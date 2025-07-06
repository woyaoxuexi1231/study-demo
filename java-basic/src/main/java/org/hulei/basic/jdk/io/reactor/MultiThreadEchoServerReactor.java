package org.hulei.basic.jdk.io.reactor;

import org.hulei.basic.jdk.io.NIOUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"resource", "CallToPrintStackTrace"})
class MultiThreadEchoServerReactor {
    ServerSocketChannel serverSocket;
    /**
     * 使用一个 AtomicInteger 来做 轮询分配，把新连接平均分配到不同的子反应器
     */
    AtomicInteger next = new AtomicInteger(0);
    Selector bossSelector = null;
    Reactor bossReactor = null;
    // selectors集合,引入多个selector选择器
    Selector[] workSelectors = new Selector[2];
    // 引入多个子反应器
    Reactor[] workReactors = null;


    MultiThreadEchoServerReactor() throws IOException {

        // 初始化 Boss Reactor（主反应器）用于监听 OP_ACCEPT 新连接事件
        bossSelector = Selector.open();

        // 初始化多个 Work Reactor（子反应器），用于处理读写 I/O 事件
        workSelectors[0] = Selector.open();
        workSelectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8103);
        serverSocket.socket().bind(address);
        // 非阻塞
        serverSocket.configureBlocking(false);

        // 第一个selector,负责监控新连接事件
        SelectionKey sk = serverSocket.register(bossSelector, SelectionKey.OP_ACCEPT);
        // 附加新连接处理handler处理器到SelectionKey（选择键）
        sk.attach(new AcceptorHandler());

        // 处理新连接的反应器
        bossReactor = new Reactor(bossSelector);

        // 第一个子反应器，一子反应器负责一个选择器
        Reactor subReactor1 = new Reactor(workSelectors[0]);
        // 第二个子反应器，一子反应器负责一个选择器
        Reactor subReactor2 = new Reactor(workSelectors[1]);
        workReactors = new Reactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        new Thread(bossReactor).start();
        // 一子反应器对应一条线程
        new Thread(workReactors[0]).start();
        new Thread(workReactors[1]).start();
    }

    // 反应器
    static class Reactor implements Runnable {
        // 每条线程负责一个选择器的查询
        final Selector selector;

        public Reactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 单位为毫秒
                    selector.select(1);
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    if (null == selectedKeys || selectedKeys.isEmpty()) {
                        continue;
                    }
                    for (SelectionKey sk : selectedKeys) {
                        NIOUtil.info("Reactor 收到事件 " + sk.interestOps());
                        // Reactor负责dispatch收到的事件
                        dispatch(sk);
                    }
                    selectedKeys.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        void dispatch(SelectionKey sk) {
            if (!sk.isValid()) {
                return; // 跳过无效 Key
            }
            Runnable handler = (Runnable) sk.attachment();
            // 调用之前attach绑定到选择键的handler处理器对象
            if (handler != null) {
                handler.run();
            }
        }
    }


    // Handler:新连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                NIOUtil.info("接收到一个新的连接");

                if (channel != null) {
                    int index = next.get();
                    NIOUtil.info("选择器的编号：" + index);
                    Selector selector = workSelectors[index];
                    new MultiThreadEchoHandler(selector, channel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (next.incrementAndGet() == workSelectors.length) {
                next.set(0);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor server =
                new MultiThreadEchoServerReactor();
        server.startService();
    }

}
