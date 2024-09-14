package org.hulei.jdk.nio.reactor;

import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2024/9/13 21:34
 */

@SuppressWarnings("CallToPrintStackTrace")
@ToString
@Slf4j
public class MultiThreadEchoHandler implements Runnable {

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    // 默认状态是读数据的状态,表示新连接刚上来
    int state = RECIEVING;

    ThreadPoolExecutor poolExecutor;

    /**
     * 创建一个新连接的请求处理类
     *
     * @param selector 选择器
     * @param c        新连接的 socket信道
     */
    @SneakyThrows
    MultiThreadEchoHandler(Selector selector, SocketChannel c) {
        poolExecutor = new ThreadPoolExecutor(
                5,
                5,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());
        byteBuffer.clear();
        channel = c;
        // 非阻塞的
        channel.configureBlocking(false);
        // 仅仅把信道注册到选择器上,但是不监听任何事件
        // 这里遇到一个问题,在这一行被阻塞了,导致后面所有的操作都不可用了,通过 jstack 查看线程占用情况可以看到
        // 在 selector.select()方法的调用栈中, at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86) 这个方法会锁住一个 publicKeys的对象
        // 而在 channel.register 的调用栈中, at sun.nio.ch.SelectorImpl.register(SelectorImpl.java:132) 这个方法也需要持有一个 publicKeys的对象
        // 但是 selector.select()我们阻塞的执行了,在底层还在执行epoll,他不会释放这个锁,所以导致 register也阻塞了, 所以select这里要使用非阻塞形式的
        // 单线程的这里没有解决这个问题是因为单线程处理的逻辑到这里的时候 selector.select已经放开了
        sk = channel.register(selector, 0);
        // 将 Handler 作为选择键的附件
        sk.attach(this);
        // 在选择器内注册好再加上把处理类都注册好之后, 再注册读事件
        sk.interestOps(SelectionKey.OP_READ);
        // 1. 当 B 线程阻塞在 select() 或 select(long) 方法上时, A线程调用 wakeup 后, B线程会立刻返回
        // 2. 如果没有线程阻塞在 select() 方法上, 那么下一次某个线程调用 select() 或 select(long) 方法时, 会立刻返回.
        // 所以, 当在 Handler 中完成 SocketChannel 注册后, 显示的调用 selector.wakeup() 方法, 虽然当前没有阻塞在 select() 上, 但是会影响下一次调用 select().
        selector.wakeup();
    }

    @Override
    public void run() {
        poolExecutor.execute(() -> {
            // MultiThreadEchoHandler.this 这个表示当前外部类的实例对象
            synchronized (MultiThreadEchoHandler.this) {
                try {
                    // 从通道读
                    int length = 0;
                    while ((length = channel.read(byteBuffer)) > 0) {
                        log.info("收到数据: {}", new String(byteBuffer.array(), 0, length));
                    }
                    if (length == -1) {
                        // 如果连接断开,那么直接取消绑定key
                        log.info("客户端的连接已经断开,取消绑定key");
                        sk.cancel();
                        return;
                    }
                    // 读完后，准备开始写入通道,byteBuffer切换成读模式
                    byteBuffer.flip();
                    // LockSupport.parkNanos((long) 1000 * 1000 * 1000);
                    // 读取数据后,立马注册写事件,主要是现在这个程序的功能主要用于回显,读取到数据之后立马准备返回去,注册写事件也是为了同一时间的其他读事件不干扰
                    int write = channel.write(byteBuffer);
                    log.info("写入了 {} 个字节", write);
                    byteBuffer.clear();
                    // 处理结束了, 这里不能关闭select key，需要重复使用
                    // sk.cancel();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // SelectionKey.cancel() 是一个方法, 它用于取消选择键的注册. 每当一个通道向选择器注册时, 都会创建一个选择键
                    // 选择键在以下情况下保持有效: 通过调用其 cancel 方法取消, 通过关闭其通道, 或通过关闭其选择器
                    // 取消选择键不会立即从选择器中移除它: 它会被添加到选择器的已取消键集中，在下一次 select 操作期间移除, 可以使用 SelectionKey.isValid() 方法来测试键的有效性
                    sk.cancel();
                    try {
                        channel.finishConnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
