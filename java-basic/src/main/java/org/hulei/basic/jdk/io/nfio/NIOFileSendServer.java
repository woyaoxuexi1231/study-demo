package org.hulei.basic.jdk.io.nfio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author hulei
 * @since 2024/9/13 12:15
 */

@SuppressWarnings({"resource", "CallToPrintStackTrace"})
public class NIOFileSendServer {

    @SneakyThrows
    public static void main(String[] args) {

        // 文件信道
        FileChannel fileChannel = null;
        // socket信道
        SocketChannel socketChannel = null;
        Socket socket = null;


        try {
            // ============================================= 创建 fileChannel ==========================================
            // 创建读取文件数据的 fileChannel
            String srcPath = NIOUtil.getResourcePath("/system.properties");
            File file = new File(srcPath);
            fileChannel = new FileInputStream(file).getChannel();

            // ============================================= 创建 socketChannel ==========================================
            // 创建一个套接字传输通道
            socketChannel = SocketChannel.open();
            /*
            启用TCP_NODELAY选项（禁用Nagle算法）。
            Nagle算法 是为了解决网络上小包过多的问题，通过将小数据包合并后再发送来减少网络中的小包数量，优化带宽利用率
            然而，在某些情况下，这种合并会带来延迟，尤其是在需要低延迟的应用场景中，例如：实时网络游戏，高频交易系统
             */
            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            socket = socketChannel.socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 18888));
            // 设置非阻塞式
            socketChannel.configureBlocking(false);

            System.out.println("Client 成功连接服务端");
            // 由于设置了非阻塞模式,所以可能连接还没有准备好,这里在连接准备好之前可以干一些其他事情
            while (!socketChannel.finishConnect()) {
                // 不断的自旋、等待，或者做一些其他的事情
            }
            System.out.printf("socket: %s%n", socket.getLocalPort());

            // ============================================= 发送数据 ==========================================
            /*
            发送之前,发送方和接收方需要确认一下数据包的格式 确认一下文件的格式(可以理解为通信协议),这里做了以下规定
            协议分为三个部分 1.文件名长度 2.文件名 3.文件长度 4.文件内容
             */


            // ====================== 1.发送文件名长度 ==================
            // 这里确定一个文件名,然后把文件名转成 byte[], 这里对于文件名单独使用一个 buffer, 方便确认这个文件名在发送的时候到底使用了多大的 buffer
            ByteBuffer fileNameBuffer = StandardCharsets.UTF_8.encode("nio_file_send_and_receive_" + file.getName());
            // 返回 fileNameByteBuffer 的 position 位置来确定文件名有多长
            int fileNameLen = fileNameBuffer.remaining();

            // 确认发送缓冲区的大小为 1024, 此时buffer的状态是写入状态
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.putInt(fileNameLen);
            // 切换到读模式，方便socketChannel来读取数据，然后发送出去
            buffer.flip();
            // 发送文件名的长度
            socketChannel.write(buffer);
            System.out.printf("Client 文件名称长度发送完成: %s%n", fileNameLen);

            // ====================== 2.发送文件名 ==================
            // 发送文件名称
            socketChannel.write(fileNameBuffer);
            System.out.printf("Client 文件名称发送完成: %s%n", file.getName());

            // ====================== 3.发送文件长度 ==================
            // clear后清空buffer的内容，再次进入写模式
            buffer.clear();
            buffer.putInt((int) file.length());
            // 切换到读模式
            buffer.flip();
            // 写入文件长度
            socketChannel.write(buffer);
            System.out.printf("Client 文件长度发送完成: %s%n", file.length());


            // ====================== 4.发送文件 ==================
            // 发送文件内容
            System.out.println("======== 开始传输文件 ========");
            int length = 0;
            long offset = 0;
            // 清空内容，并切换到写模式
            buffer.clear();
            // 从 channel 中读取数据 写入到 buffer 中
            while ((length = fileChannel.read(buffer)) > 0) {
                // 反转
                buffer.flip();
                // 从 buffer 中读取数据，通过 socketchannel 发送到客户端
                socketChannel.write(buffer);
                // 清空内容，切换到写模式
                buffer.clear();
                // 计算一下进度
                offset += length;
                System.out.printf("| %s%% |", 100 * offset / file.length());
                // 每次发送停顿 3 秒
                // LockSupport.parkNanos(3L * 1000 * 1000 * 1000);
            }

            // 等待一分钟关闭连接
            // LockSupport.parkNanos(60 * 1000L * 1000L * 1000L);
            System.out.println("======== 文件传输成功 ========");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fileChannel.close();
            socketChannel.close();
            socket.close();
        }
    }

}
