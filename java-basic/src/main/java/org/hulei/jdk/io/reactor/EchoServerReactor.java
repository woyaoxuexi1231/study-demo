package org.hulei.jdk.io.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

@SuppressWarnings({"CallToPrintStackTrace", "resource"})
@Slf4j
public class EchoServerReactor implements Runnable {

    Selector acceptSelector;

    EchoServerReactor() throws IOException {

        // 创建选择器和服务监听套接字
        acceptSelector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置监听端口,开始监听
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8102);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已经开始监听：{}", address);
        // 非阻塞
        serverSocketChannel.configureBlocking(false);


        // 分步处理,第一步,接收accept事件
        SelectionKey sk = serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
        // 将对象附加到 SelectionKey 上, attach会搭配 attachment()方法一起使用, 这里attach之后, 在后面处理的时候会通过 attachment()拿出来, 埋点数据
        sk.attach((Runnable) () -> {
            try {
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    log.info("接收到一个连接 socket: {}", channel.socket().getPort());
                    new EchoHandler(acceptSelector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        try {
            // 除非线程中断, 否则程序会一直跑
            while (!Thread.interrupted()) {
                // 阻塞的去查询选择器上是否有事件了,一旦有事件这个方法会立马返回
                acceptSelector.select();
                // 遍历所有准备好的键
                Set<SelectionKey> selected = acceptSelector.selectedKeys();
                for (SelectionKey sk : selected) {
                    // Reactor负责 dispatch 收到的事件
                    dispatch(sk);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void dispatch(SelectionKey sk) {
        // 实际分发的时候再获取之前的定义的分发逻辑进行具体的分发
        Runnable handler = (Runnable) sk.attachment();
        // 调用之前 attach 绑定到选择键的 handler 处理器对象
        if (handler != null) {
            handler.run();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}
