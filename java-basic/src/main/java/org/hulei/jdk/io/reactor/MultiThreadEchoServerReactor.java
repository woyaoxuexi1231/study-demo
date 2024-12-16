package org.hulei.jdk.io.reactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 多线程版本的 reactor 分发器, 考虑从下面两个方面进行多线程的延伸
 * 1. 单线程版本的 selector 就一个, 对于连接事件的检测和读写事件的检测可以分别使用不同的 selector 来完成
 * 2. 单线程版本的 handler 处理都也堆在主线程内执行, 可以考虑把业务处理变成多线程处理(线程池)
 *
 * @author hulei
 * @since 2024/9/13 21:09
 */

@SuppressWarnings({"resource", "CallToPrintStackTrace"})
@Slf4j
public class MultiThreadEchoServerReactor implements Runnable {
    /**
     * 用于检测连接事件的选择器
     */
    Selector acceptSelector;
    /**
     * 用于检测读写事件的选择器
     */
    Selector[] workerSelectors = new Selector[2];

    int index = 0;

    MultiThreadEchoServerReactor() throws IOException {

        // 创建选择器和服务监听套接字
        acceptSelector = Selector.open();
        // 初始化读写事件选择器
        workerSelectors[0] = Selector.open();
        workerSelectors[1] = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置监听端口,开始监听
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8103);
        serverSocketChannel.socket().bind(address);
        log.info("服务端已经开始监听：{}", address);
        // 非阻塞
        serverSocketChannel.configureBlocking(false);

        // 分步处理,第一步,接收accept事件
        SelectionKey sk = serverSocketChannel.register(acceptSelector, 0);
        // 将对象附加到 SelectionKey 上, attach会搭配 attachment()方法一起使用, 这里attach之后, 在后面处理的时候会通过 attachment()拿出来, 埋点数据
        sk.attach((Runnable) () -> {
            try {
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    log.info("接收到一个连接 socket: {}", channel.socket().getPort());
                    MultiThreadEchoHandler handler = new MultiThreadEchoHandler(workerSelectors[(index = (index + 1) % 2)], channel);
                }
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
        });
        sk.interestOps(SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        try {
            // 除非线程中断, 否则程序会一直跑
            while (true) {
                // 阻塞的去查询选择器上是否有事件了,一旦有事件这个方法会立马返回
                acceptSelector.select();
                // 遍历所有准备好的键
                Set<SelectionKey> selected = acceptSelector.selectedKeys();
                for (SelectionKey sk : selected) {
                    // Reactor负责 dispatch 收到的事件
                    EchoServerReactor.dispatch(sk);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 这里弄着弄着发现一个问题
        // 多线程的情况下
        // 1.如果复用EchoHandler.run的逻辑,在切换到写事件的同时,这里的selector会一直触发写事件,那么写事件的任务会堆积在线程池内
        // 2.在客户端断开连接的情况下,服务器收到FIN消息,没有进行处理,这里会一直触发读事件
        // 上面两个都会造成非常大的CPU资源消耗, TODO 这个怎么解决呢
        MultiThreadEchoServerReactor acceptReactor = new MultiThreadEchoServerReactor();
        new Thread(acceptReactor,"accept_thread").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 除非线程中断, 否则程序会一直跑
                    while (true) {
                        // 阻塞的去查询选择器上是否有事件了,一旦有事件这个方法会立马返回
                        acceptReactor.workerSelectors[0].select(100L);
                        // 遍历所有准备好的键
                        Set<SelectionKey> selected = acceptReactor.workerSelectors[0].selectedKeys();
                        for (SelectionKey sk : selected) {
                            // Reactor负责 dispatch 收到的事件
                            EchoServerReactor.dispatch(sk);
                        }
                        selected.clear();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        },"work_1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 除非线程中断, 否则程序会一直跑
                    while (true) {
                        // 阻塞的去查询选择器上是否有事件了,一旦有事件这个方法会立马返回
                        acceptReactor.workerSelectors[1].select(100L);
                        // 遍历所有准备好的键
                        Set<SelectionKey> selected = acceptReactor.workerSelectors[1].selectedKeys();
                        if(!CollectionUtils.isEmpty(selected)){
                            log.info("有读事件触发");
                        }
                        for (SelectionKey sk : selected) {
                            // Reactor负责 dispatch 收到的事件
                            EchoServerReactor.dispatch(sk);
                        }
                        selected.clear();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        },"work_2").start();

    }
}
