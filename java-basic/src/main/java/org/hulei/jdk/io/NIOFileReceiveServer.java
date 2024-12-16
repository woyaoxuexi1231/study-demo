package org.hulei.jdk.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author hulei
 * @since 2024/9/13 12:15
 */

@SuppressWarnings("resource")
@Slf4j
public class NIOFileReceiveServer {

    // 使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    static Map<SelectableChannel, NIOUtil.Session> clientMap = new HashMap<SelectableChannel, NIOUtil.Session>();

    private static final ByteBuffer buffer = ByteBuffer.allocate(1024);

    @SneakyThrows
    public static void main(String[] args) {
        // 创建一个服务端的通信信道, 以这个信道创建一个可以通信的 socket
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        // 设置这个 socket 为非阻塞,绑定的端口为 8888
        serverChannel.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(8888));

        // 将通道注册到选择器上, 并注册的IO事件为：“接收新连接”
        Selector selector = Selector.open();
        // 每个channel最多只能向 selector注册一次,注册之后就形成了固定的 selectionKey
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("serverChannel is listening...");

        int select;
        // 阻塞当前线程,直到至少有一个通道准备好进行I/O操作,选择器内有信道准备好之后会返回
        while ((select = selector.select()) > 0) {

            // 返回 Selector中的所有已准备好进行I/O操作的通道的 SelectionKey集合。
            if (null == selector.selectedKeys()) {
                continue;
            }
            // 7、遍历所有已经准备好的 SelectionKey集合
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {

                // 8、获取单个的选择键，并处理
                SelectionKey key = it.next();
                if (null == key) {
                    continue;
                }

                // 9、如果是一个建立新连接的操作
                if (key.isAcceptable()) {

                    // 10、若接受的事件是“新连接”事件, 再通过 SelectionKey 获取这个 key对应的信道
                    ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = ssChannel.accept();
                    if (socketChannel == null) {
                        continue;
                    }

                    // 11、客户端新连接，切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);

                    // 这个新连接将作为一个新的信道注册到 selector 上, 并且注册一个读事件
                    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    // 余下为业务处理
                    NIOUtil.Session session = new NIOUtil.Session();
                    session.remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, session);
                    log.info("{} 连接成功...", socketChannel.getRemoteAddress());

                } else if (key.isReadable()) {
                    // 如果这是一个可读的事件, 那么就扔去处理数据的方法
                    handleData(key);
                }
                // NIO的特点只会累加，已选择的键的集合不会删除
                // 如果不删除，下一次又会被select函数选中
                it.remove();
            }
        }
    }

    /**
     * 处理客户端传输过来的数据
     */
    private static void handleData(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        NIOUtil.Session session = clientMap.get(key.channel());
        // 写模式
        buffer.clear();
        // 在客户端已经关闭的情况下, 这里的读事件依旧会触发, 但是使用 read() 方法得到的数字是 -1
        //   TCP    127.0.0.1:8888         127.0.0.1:10355        CLOSE_WAIT      30284
        //   TCP    127.0.0.1:10355        127.0.0.1:8888         FIN_WAIT_2      30920
        // 关闭过程为
        //          客户端FIN(客户端进入FIN_WAIT_1) -> 服务端收到FIN,发送ACK(服务端进入CLOSE_WAIT)
        //          -> 客户端收到ACK(进入FIN_WAIT_2),等待服务端的FIN -> 服务端发送FIN(进入LAST_ACK状态)
        //          -> 客户端收到FIN,发送ACK(进入TIME_WAIT,一段时间后变为CLOSED状态) -> 服务器收到ACK(进入CLOSED状态)
        // 由此可见,这里停在了服务端没有发送FIN请求给客户端
        while ((num = socketChannel.read(buffer)) > 0) {
            log.info("收到的字节数 = {}", num);
            // 切换到读模式
            buffer.flip();
            process(session, buffer);
            // 读完之后切换回写模式
            buffer.clear();
        }
        if (num == -1) {
            // 如果连接断开,那么直接取消绑定key
            log.info("客户端的连接已经断开,取消绑定key");
            key.cancel();
        }
        if (num == 0) {
            log.info("此次数据已经读取完成.");
        }
    }

    @SneakyThrows
    private static void process(NIOUtil.Session session, ByteBuffer buffer) {
        /*
        这里把消息处理一共分为四步, 和客户端规定的协议一致
        不管一次性获取到多少个字节的消息,按照如下顺序进行解析
        文件名长度 -> 文件名 -> 文件长度 -> 文件内容
         */
        while (NIOUtil.len(buffer) > 0) {

            // 第一步处理文件名长度
            if (1 == session.step) {
                // 检查是否出现半包问题,这可能导致我们拿不到正确的文件名长度
                // 半包问题就是客户端本来发的是 ABC, 但是由于传输层协议拆包或者其他原因, 导致服务端先收到了 AB, 再收到 C
                // TODO netty怎么解决的?
                if (buffer.remaining() < 4) {
                    log.info("出现半包问题，需要更加复杂的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复杂的拆包方案");
                }
                // 获取文件名称长度
                session.fileNameLength = buffer.getInt();
                System.out.println("读取文件名称长度 = " + session.fileNameLength);
                session.step = 2;

            } else if (2 == session.step) {
                // 第二步, 处理文件名
                if (NIOUtil.len(buffer) < session.fileNameLength) {
                    log.info("出现半包问题，需要更加复杂的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }
                // 按照第一步收到的文件名长度读取固定长度的内容
                byte[] fileNameBytes = new byte[session.fileNameLength];
                // 读取文件名称
                buffer.get(fileNameBytes);
                // 文件名
                String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
                System.out.println("读取文件名称 = " + fileName);
                session.fileName = fileName;
                NIOUtil.createDir();
                // 确定上传的文件的文件名
                File file = new File(NIOUtil.getResourcePath("/upload/" + fileName));
                session.fileChannel = new FileOutputStream(file).getChannel();
                session.step = 3;
            } else if (3 == session.step) {
                // 第三步,处理文件大小
                if (buffer.remaining() < 4) {
                    log.info("出现半包问题，需要更加复杂的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复杂的拆包方案");
                }
                // 获取文件内容长度
                session.fileLength = buffer.getInt();
                System.out.println("读取文件内容长度 = " + session.fileLength);
                session.step = 4;
                session.startTime = System.currentTimeMillis();
            } else if (4 == session.step) {
                // 第四步解析文件内容
                session.receiveLength += NIOUtil.len(buffer);
                // 写入文件
                session.fileChannel.write(buffer);
                if (session.isFinished()) {
                    log.info("文件接收成功, File Name：{}, Size: {}, NIO传输时间: {}", session.fileName, NIOUtil.getFormatFileSize(session.fileLength), System.currentTimeMillis() - session.startTime);
                }
            }
        }
    }
}
