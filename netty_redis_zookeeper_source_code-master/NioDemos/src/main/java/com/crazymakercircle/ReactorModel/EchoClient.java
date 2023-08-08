package com.crazymakercircle.ReactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Dateutil;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public class EchoClient {

    public void start() throws IOException {

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);

        // 1、获取通道
        SocketChannel socketChannel = SocketChannel.open(address);
        Logger.info("客户端连接成功");
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        // 禁用 Nagle 算法, Nagle 算法是通过合并短段并提高网络效率, 当我们只要发送 1 字节的数据, 却需要 40 字节的 TCP/IP 头部时, 浪费会非常大
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        //不断的自旋、等待连接完成, 或者做一些其他的事情
        while (!socketChannel.finishConnect()) {

        }
        Logger.tcfo("客户端启动成功！");

        // 启动接受线程
        Processor processor = new Processor(socketChannel);
        Commander commander = new Commander(processor);
        new Thread(commander).start();
        new Thread(processor).start();

    }

    static class Commander implements Runnable {
        Processor processor;

        Commander(Processor processor) throws IOException {
            //Reactor初始化
            this.processor = processor;
        }

        public void run() {
            while (!Thread.interrupted()) {
                // 获取发送缓冲
                ByteBuffer buffer = processor.getSendBuffer();
                // 获取输入
                Scanner scanner = new Scanner(System.in);
                while (processor.hasData.get()) {
                    Logger.tcfo("还有消息没有发送完，请稍等");
                    ThreadUtil.sleepMilliSeconds(1000);
                }
                Logger.tcfo("请输入发送内容:");
                // 开始阻塞, 等待键入内容
                if (scanner.hasNext()) {
                    // 读取键入内容
                    String next = scanner.next();
                    // 塞入发送缓冲
                    buffer.put((Dateutil.getNow() + " >>" + next).getBytes());
                    // 设置当前为有数据状态
                    processor.hasData.set(true);
                }

            }
        }
    }


    @Data
    static class Processor implements Runnable {
        // 申请一个发送缓冲区 send.buffer.size=1024
        ByteBuffer sendBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);
        // 申请一个读取缓冲区 send.buffer.size=1024
        ByteBuffer readBuffer = ByteBuffer.allocate(NioDemoConfig.SEND_BUFFER_SIZE);

        protected AtomicBoolean hasData = new AtomicBoolean(false);

        final Selector selector;
        final SocketChannel channel;

        Processor(SocketChannel channel) throws IOException {
            // Reactor 初始化
            selector = Selector.open();
            this.channel = channel;
            // 注册当前通道感兴趣的事件为 OP_READ 和 OP_WRITE
            channel.register(selector,
                    SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 等待 IO 事件
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    // 消费 IO 事件
                    for (SelectionKey sk : selected) {
                        /*
                        判断事件类型, 在 EchoHandler 中, sk.interestOps(SelectionKey.OP_READ) 注册了一个读事件
                        这里开始, 我们会得到 sk.isWritable() = true 的结果, 意味着现在这个 sk 是可写的
                        key.isWritable()是表示Socket可写, 网络不出现阻塞情况下, 一直是可以写的, 所认一直为true. 一般我们不注册OP_WRITE事件.
                         */
                        if (sk.isWritable()) {
                            // 一直循环进入这里进行判断当前缓冲内是否有数据可用读取
                            if (hasData.get()) {
                                // 开启一个通道
                                SocketChannel socketChannel = (SocketChannel) sk.channel();
                                // 反转一下发送缓冲为读模式
                                sendBuffer.flip();
                                // 发送缓冲的数据写入通道
                                socketChannel.write(sendBuffer);
                                // 把缓冲区切换为写模式, 清空 position, limit 设置为 capacity 最大容量值
                                sendBuffer.clear();
                                // 读完之后, 设置当前为没有数据的状态
                                hasData.set(false);
                            }
                        }
                        if (sk.isReadable()) {
                            // 收到一个可读的 IO 事件
                            SocketChannel socketChannel = (SocketChannel) sk.channel();
                            int length;
                            while ((length = socketChannel.read(readBuffer)) > 0) {
                                readBuffer.flip();
                                Logger.info("server echo:" + new String(readBuffer.array(), 0, length));
                                readBuffer.clear();
                            }

                        }
                        //处理结束了, 这里不能关闭 select key, 需要重复使用
                        //selectionKey.cancel();
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
