package com.crazymakercircle.ReactorModel;


import com.crazymakercircle.util.Logger;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MultiThreadEchoHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;
    /**
     * 引入线程池
     */
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    MultiThreadEchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        channel.configureBlocking(false);
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);

        // 唤醒选择, 防止 register 时 boss 线程被阻塞, netty 处理方式比较优雅, 会在同一个线程注册事件, 避免阻塞 boss
        // selector.wakeup();

        // 仅仅取得选择键, 后设置感兴趣的 IO 事件
        sk = channel.register(selector, 0);
        //将本 Handler 作为 sk 选择键的附件, 方便事件 dispatch
        sk.attach(this);
        //向 sk 选择键注册 Read 就绪事件
        sk.interestOps(SelectionKey.OP_READ);

        //唤醒选择, 是的 OP_READ 生效
        selector.wakeup();
        Logger.info("新的连接 注册完成");

    }

    public void run() {
        /*
        异步任务, 在独立的线程池中执行
        每当触发 OP_READ 事件的时候, 会由 workReactors 分发这些请求, 然后拿到 SelectionKey 里面 attach 的这个对象
        最后触发这个对象的 run 方法
         */
        pool.execute(new AsyncTask());
    }

    public synchronized void asyncRun() {
        try {
            if (state == SENDING) {
                // 写入通道
                channel.write(byteBuffer);
                // 写完后, 准备开始从通道读, byteBuffer 切换成写模式
                byteBuffer.clear();
                // 写完后, 注册 read 就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后, 进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 从通道读
                int length;
                while ((length = channel.read(byteBuffer)) > 0) {
                    Logger.info(new String(byteBuffer.array(), 0, length));
                }
                // 读完后, 准备开始写入通道, byteBuffer 切换成读模式
                byteBuffer.flip();
                // 读完后, 注册 write 就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                // 读完后, 进入发送的状态
                state = SENDING;
            }
            // 处理结束了, 这里不能关闭 select key, 需要重复使用, 也由于这里没有关闭, 所以这里一直在触发 IO 事件, 一直在触发 asyncRun() 这个方法
            // sk.cancel();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 异步任务的内部类
     */
    class AsyncTask implements Runnable {
        public void run() {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }

}

