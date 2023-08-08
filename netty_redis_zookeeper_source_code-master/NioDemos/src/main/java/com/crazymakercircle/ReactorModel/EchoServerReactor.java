package com.crazymakercircle.ReactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

//反应器
class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        // Reactor 初始化, 获取一个 Selector 实例
        selector = Selector.open();
        // 获取通道
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        // 绑定连接
        serverSocket.socket().bind(address);
        Logger.info("服务端已经开始监听：" + address);
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理,第一步,接收 accept 事件
        SelectionKey sk =
                serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, AcceptorHandler - 一个用于处理 accept 请求的处理器
        // 将对象附加到选择器
        sk.attach(new AcceptorHandler());
    }

    public void run() {
        // main方法执行完成后的某个时间点, 这个线程开始运行
        try {
            // 只要当前线程不被终端, 就一直运行
            while (!Thread.interrupted()) {
                /*
                select 方法会选出已经注册的、已经就绪的 IO 事件, 并且保存到 selectedKeys 集合里
                select() - 调用阻塞, 直到至少有一个通道发生了注册的 IO 事件
                select(long timeout) - 阻塞, 但最长阻塞事件为 timeout 指定的毫秒数
                selectNow() - 非阻塞, 无论有没有 IO 事件都会立刻返回
                返回值表示 IO 事件的数量
                 */
                selector.select();
                // 这里 select() 方法返回, 说明有 IO 事件发生了, 获取 selectedKeys 集合
                Set<SelectionKey> selected = selector.selectedKeys();
                int i = 0;
                for (SelectionKey sk : selected) {
                    // Reactor 负责 dispatch 收到的事件
                    dispatch(sk);
                }
                // 处理完成后需要将选择键从 selectedKeys 集合中移除, 表示这个事件已经被处理过了, 以免下一次重复处理
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        // attachment() - 从选择键中获取附加对象, 这个对象就是在每次有新连接的时候在 selector 里绑定的 EchoHandler
        Runnable handler = (Runnable) sk.attachment();
        //调用之前 attach 绑定到选择键的 handler 处理器对象, 也就是 EchoHandler 的 run 方法
        if (handler != null) {
            handler.run();
        }
    }

    class AcceptorHandler implements Runnable {
        // Handler : 新连接处理器
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                Logger.info("接收到一个连接");
                if (channel != null) {
                    // 构造方法内完成了 EchoHandler 与 selector 的绑定, 并且注册感兴趣的事件 (OP_READ)
                    new EchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
