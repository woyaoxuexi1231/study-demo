package org.hulei.basic.jdk.io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 单线程版本的 Reactor 参考代码，代码参考来自 《Java高并发核心编程卷1》
 * 单线程的 Reactor 主要通过 attach 和 attachment 结合使用
 *
 * @author hulei
 * @since 2025/7/5 23:33
 */
@SuppressWarnings({"CallToPrintStackTrace", "resource"})
public class EchoServerReactor implements Runnable {

    /**
     * 监听连接请求的选择器
     */
    Selector selector;

    EchoServerReactor() throws IOException {

        /*
        EchoServerReactor 构造函数主要完成的任务：
            1. 声明服务端监听端口，并开启监听
            2. 监听信道和选择器的绑定，以及注册连接事件
            3. 定义连接事件触发后，如何处理连接事件
         */

        // 创建选择器和服务监听套接字
        selector = Selector.open();

        // 设置监听端口,开始监听
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8102);
        serverSocketChannel.socket().bind(address);
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.printf("服务端已经开始监听：%s%n", address);

        /*
        SelectionKey 是 Java NIO 中连接 Selector 和 Channel 的关键对象，它代表了通道在选择器中的注册关系。
        这里是把服务监听套接字的信道绑定到选择器上，并且注册 ACCEPT 事件，也就是连接事件
         */
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 将对象附加到 SelectionKey 上, attach会搭配 attachment() 方法一起使用, 这里attach之后, 在后面处理的时候会通过 attachment()拿出来, 埋点数据
        sk.attach((Runnable) () -> {
            try {
                // 这里的逻辑就是，收到连接事件后，为每个连接新建一个 EchoHandler 来处理后续的操作，然后 EchoServerReactor 的工作就算是结束了
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    System.out.printf("接收到一个连接 socket: %s%n", channel.socket().getPort());
                    new EchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        /*
        run 方法会 EchoServerReactor 构造函数后执行
        主要完成的内容：
            1.线程将不停的轮询查询选择器是否有就绪的事件
            2.拿到事件后通过 attachment 方法取出在 EchoServerReactor构造函数或者 EchoHandler 注册的处理逻辑来执行
        总结来说 这里就是进行事件分发的

        这个代码我其实有一点想法就是 dispatch 的部分是不是不放这里会更容易理解，把 EchoServerReactor 和 EchoHandler 单独抽开
         */
        try {
            // 除非线程中断, 否则程序会一直跑
            while (!Thread.interrupted()) {
                // 阻塞的去查询选择器上是否有事件了,一旦有事件这个方法会立马返回
                selector.select();
                // 遍历所有准备好的键
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey sk : selected) {
                    // Reactor负责 dispatch 收到的事件
                    dispatch(sk);
                }
                // 显式调用 selected.clear()，把集合清空，保证下次只处理新的就绪事件。
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
