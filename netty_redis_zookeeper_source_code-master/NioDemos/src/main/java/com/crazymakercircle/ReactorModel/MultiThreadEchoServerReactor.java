package com.crazymakercircle.ReactorModel;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


class MultiThreadEchoServerReactor {
    ServerSocketChannel serverSocket;
    AtomicInteger next = new AtomicInteger(0);
    /**
     * 用于监听新连接的 Selector
     */
    Selector bossSelector;
    /**
     * 用于分发新连接的 Reactor
     */
    Reactor bossReactor;
    /**
     * selectors集合, 引入多个 selector 选择器
     */
    Selector[] workSelectors = new Selector[2];
    /**
     * 引入多个子反应器
     */
    Reactor[] workReactors;


    MultiThreadEchoServerReactor() throws IOException {
        // 初始化监听 Selector
        bossSelector = Selector.open();
        // 初始化工作 Selector
        workSelectors[0] = Selector.open();
        workSelectors[1] = Selector.open();
        serverSocket = ServerSocketChannel.open();

        InetSocketAddress address =
                new InetSocketAddress(NioDemoConfig.SOCKET_SERVER_IP,
                        NioDemoConfig.SOCKET_SERVER_PORT);
        serverSocket.socket().bind(address);
        // 非阻塞
        serverSocket.configureBlocking(false);

        // 第一个 selector, 负责监控新连接事件
        SelectionKey sk =
                serverSocket.register(bossSelector, SelectionKey.OP_ACCEPT);
        // 附加新连接处理 handler 处理器到 SelectionKey
        sk.attach(new AcceptorHandler());

        // 处理新连接的反应器
        bossReactor = new Reactor(bossSelector);

        // 第一个子反应器, 一子反应器负责一个选择器
        Reactor subReactor1 = new Reactor(workSelectors[0]);
        // 第二个子反应器, 一子反应器负责一个选择器
        Reactor subReactor2 = new Reactor(workSelectors[1]);
        workReactors = new Reactor[]{subReactor1, subReactor2};
    }

    private void startService() {
        new Thread(bossReactor).start();
        // 一子反应器对应一条线程
        new Thread(workReactors[0]).start();
        new Thread(workReactors[1]).start();
    }


    static class Reactor implements Runnable {
        /**
         * 每条线程 (Reactor) 负责一个选择器的查询
         */
        final Selector selector;

        public Reactor(Selector selector) {
            this.selector = selector;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    // 单位为毫秒
                    selector.select(1000);
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    if (null == selectedKeys || selectedKeys.size() == 0) {
                        continue;
                    }
                    for (SelectionKey sk : selectedKeys) {
                        // Reactor 负责 dispatch 收到的事件
                        dispatch(sk);
                    }
                    selectedKeys.clear();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        void dispatch(SelectionKey sk) {
            Runnable handler = (Runnable) sk.attachment();
            //调用之前attach绑定到选择键的handler处理器对象
            if (handler != null) {
                handler.run();
            }
        }
    }

    /**
     * 新连接的处理器类
     */
    class AcceptorHandler implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                Logger.info("接收到一个新的连接");

                if (channel != null) {
                    // 这个用于轮询使用 workSelectors
                    int index = next.get();
                    Logger.info("选择器的编号：" + index);
                    Selector selector = workSelectors[index];
                    // 在收到新连接之后, 为当前处理这个请求的 workSelectors 绑定 handler 对象
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
