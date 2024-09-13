package org.hulei.jdk.nio.reactor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;


@Slf4j
public class EchoClient {

    public void start() throws IOException {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);

        // 1、获取通道（channel）
        SocketChannel socketChannel = SocketChannel.open(address);
        log.info("客户端连接成功");
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        // 不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {

        }
        log.info("客户端启动成功！");

        // 启动接受线程
        Processor processor = new Processor(socketChannel);
        Commander commander = new Commander(processor);
        new Thread(commander).start();
        new Thread(processor).start();

    }

    static class Commander implements Runnable {
        Processor processor;

        Commander(Processor processor) throws IOException {
            // Reactor初始化
            this.processor = processor;
        }

        public void run() {
            while (!Thread.interrupted()) {

                ByteBuffer buffer = processor.getSendBuffer();

                Scanner scanner = new Scanner(System.in);
                while (processor.hasData.get()) {
                    log.info("还有消息没有发送完，请稍等, {}", processor.sendBuffer);
                    LockSupport.parkNanos(1000 * 1000L * 1000L);

                }
                log.info("请输入发送内容:");
                if (scanner.hasNext()) {

                    String next = scanner.next();
                    buffer.put((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()) + " >>" + next).getBytes());
                    processor.hasData.set(true);
                }

            }
        }
    }


    @SuppressWarnings("CallToPrintStackTrace")
    @Data
    static class Processor implements Runnable {
        ByteBuffer sendBuffer = ByteBuffer.allocate(1024);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        protected AtomicBoolean hasData = new AtomicBoolean(false);

        final Selector selector;
        final SocketChannel channel;

        Processor(SocketChannel channel) throws IOException {
            // Reactor初始化
            selector = Selector.open();
            this.channel = channel;
            // 绑定当前的 socket信道到 selector,注册两个事件,一个读事件 一个写事件
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        @Override
        public void run() {
            // 线程启动之后,会调用这里的run方法
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (SelectionKey sk : selected) {
                        /*
                        key.isWritable()是表示Socket可写, 网络不出现阻塞情况下, 一直是可以写的, 所认一直为true. 一般我们不注册OP_WRITE事件.
                         */
                        if (sk.isWritable()) {
                            if (hasData.get()) {
                                SocketChannel socketChannel = (SocketChannel) sk.channel();
                                sendBuffer.flip();
                                // 操作三：发送数据
                                socketChannel.write(sendBuffer);
                                log.info("消息发送完成, {}", sendBuffer);
                                sendBuffer.clear();
                                hasData.set(false);
                            }
                        }
                        if (sk.isReadable()) {
                            // 若选择键的IO事件是“可读”事件,读取数据
                            SocketChannel socketChannel = (SocketChannel) sk.channel();

                            int length = 0;
                            while ((length = socketChannel.read(readBuffer)) > 0) {
                                readBuffer.flip();
                                log.info("server echo:{}", new String(readBuffer.array(), 0, length));
                                readBuffer.clear();
                            }
                        }
                        // 处理结束了, 这里不能关闭select key，需要重复使用
                        // selectionKey.cancel();
                    }
                    selected.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient().start();
    }
}
