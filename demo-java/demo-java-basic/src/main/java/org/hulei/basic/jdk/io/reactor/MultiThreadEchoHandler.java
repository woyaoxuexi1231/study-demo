package org.hulei.basic.jdk.io.reactor;

import lombok.SneakyThrows;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings({"resource", "CallToPrintStackTrace"})
class MultiThreadEchoHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;
    // 引入线程池
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    MultiThreadEchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        channel.configureBlocking(false);
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);

        // 唤醒选择,防止register时 boss线程被阻塞，netty 处理方式比较优雅，会在同一个线程注册事件，避免阻塞boss
        // selector.wakeup();

        // 仅仅取得选择键，后设置感兴趣的IO事件
        sk = channel.register(selector, 0);
        // 将本Handler作为sk选择键的附件，方便事件dispatch
        sk.attach(this);
        // 向sk选择键注册Read就绪事件
        sk.interestOps(SelectionKey.OP_READ);

        // 唤醒选择，是的OP_READ生效
        selector.wakeup();
        NIOUtil.info("新的连接 注册完成");

    }

    public void run() {
        // 异步任务，在独立的线程池中执行
        pool.execute(new AsyncTask());
    }

    // 异步任务，不在Reactor线程中执行
    @SneakyThrows
    public synchronized void asyncRun() {
        try {
            /*
            问题1：既然线程池执行，这里还在执行过程中，那边又收到读事件怎么办？这边读channel会读到奇怪的数据吗？这个方法还是同步的，即使进来再多任务也不能同时执行啊？那线程池的意思是啥？
            问题2：这里进行的读事件和写事件的切换，那么这个是否会影响接受读事件的消息呢？如果客户端还在发数据怎么办？
            问题3：我发现客户端在断开连接后，这里一直会触发读事件，然后会一直报错 远程主机强迫关闭了一个现有的连接。
            问题4：这里的逻辑在切换到写事件后，会一直触发写事件，不停的进入asyncRun这个方法

            这里弄着弄着发现一个问题，多线程的情况下：
            1.如果复用EchoHandler.run的逻辑,在切换到写事件的同时,这里的selector会一直触发写事件,那么写事件的任务会堆积在线程池内
            2.在客户端断开连接的情况下,服务器收到FIN消息,没有进行处理,这里会一直触发读事件
            上面两个都会造成非常大的CPU资源消耗, TODO 这个怎么解决呢
             */
            if (state == SENDING) {
                // 写入通道
                channel.write(byteBuffer);
                NIOUtil.info("触发写事件了");
                // 写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                // 写完后,注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后,进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 从通道读
                int length = 0;
                NIOUtil.info("触发读事件了");
                // 假设每次处理需要 3 秒
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));
                while ((length = channel.read(byteBuffer)) > 0) {
                    NIOUtil.info(new String(byteBuffer.array(), 0, length));
                }
                if (length == -1) {
                    // 远程关闭了连接，自己也要关闭
                    sk.cancel();
                    channel.close();
                    return;
                }
                // 读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                // 读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                // 读完后,进入发送的状态
                state = SENDING;
            }
            // 处理结束了, 这里不能关闭select key，需要重复使用
            // sk.cancel();
        } catch (IOException ex) {
            // 远程关闭了连接，自己也要关闭
            // NIOUtil.info("IO异常，客户端可能已经断开连接了。" + ex.getMessage());
            sk.cancel();
            channel.close();
        }
    }

    // 异步任务的内部类
    class AsyncTask implements Runnable {
        public void run() {
            MultiThreadEchoHandler.this.asyncRun();
        }
    }

}

