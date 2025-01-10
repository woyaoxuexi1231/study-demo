package org.hulei.basic.jdk.io.reactor;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@SuppressWarnings("CallToPrintStackTrace")
@Slf4j
class EchoHandler implements Runnable {

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    // 默认状态是读数据的状态,表示新连接刚上来
    int state = RECIEVING;

    /**
     * 创建一个新连接的请求处理类
     *
     * @param selector 选择器
     * @param c        新连接的 socket信道
     */
    @SneakyThrows
    EchoHandler(Selector selector, SocketChannel c) {
        channel = c;
        // 非阻塞的
        c.configureBlocking(false);
        // 仅仅把信道注册到选择器上,但是不监听任何事件
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

    /**
     * 具体的处理逻辑
     * 在构造函数内,已经把当前这个handler注册到选择器上了,在分发器内分发的时候,会调用这个run方法来执行具体的逻辑
     */
    @Override
    public void run() {

        try {
            if (state == SENDING) {
                // 写入通道
                // LockSupport.parkNanos(3L * 1000 * 1000 * 1000);
                channel.write(byteBuffer);
                // log.info("写入数据 {}", new String(byteBuffer.array(), StandardCharsets.UTF_8));
                // 写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                // 写完后,注册 read 就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后,进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    log.info("收到数据: {}", new String(byteBuffer.array(), 0, length));
                }
                // 读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                // 读取数据后,立马注册写事件,主要是现在这个程序的功能主要用于回显,读取到数据之后立马准备返回去,注册写事件也是为了同一时间的其他读事件不干扰
                sk.interestOps(SelectionKey.OP_WRITE);
                // 读完后,进入发送的状态
                state = SENDING;
            }
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


}

