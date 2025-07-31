package org.hulei.basic.jdk.io.reactor;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("CallToPrintStackTrace")
class EchoHandler implements Runnable {

    /**
     * 当前处理器所绑定的网络信道
     */
    final SocketChannel channel;
    /**
     * 选择器
     */
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    /*
    默认状态是读数据的状态，对于一个新连接，肯定是要接受客户端发来的消息的
     */
    int state = RECIEVING;

    /**
     * 创建一个新连接的请求处理类
     *
     * @param selector 选择器
     * @param c        新连接的 socket信道
     */
    @SneakyThrows
    EchoHandler(Selector selector, SocketChannel c) {
        // 从EchoServerReactor传过来的网络信道
        channel = c;
        // 非阻塞的
        c.configureBlocking(false);
        /*
        这里我是有个疑问的，为什么非要先不监听任何事件，只是把通道挂上去
        gpt指出只是说这种写法更安全也更直观
         */
        sk = channel.register(selector, 0);
        // 将 Handler 作为选择键的附件
        sk.attach(this);
        // 在选择器内注册好再加上把处理类都注册好之后, 再注册读事件
        sk.interestOps(SelectionKey.OP_READ);
        /*
        1. 当 B 线程阻塞在 select() 或 select(long) 方法上时, A线程调用 wakeup 后, B线程会立刻返回
        2. 如果没有线程阻塞在 select() 方法上, 那么下一次某个线程调用 select() 或 select(long) 方法时, 会立刻返回.
        所以, 当在 Handler 中完成 SocketChannel 注册后, 显示的调用 selector.wakeup() 方法, 虽然当前没有阻塞在 select() 上, 但是会影响下一次调用 select().
        如果不 wakeup，Selector 可能还卡在 select()，没感知到新的 interestOps，就收不到这个通道的读事件了，导致连接假死。
         */
        selector.wakeup();
    }

    /**
     * handler业务逻辑，这里使用 Runnable 接口感觉并不合适，有点误导是否这个类会作为一个单独的线程去运行，其实没有
     */
    @Override
    public void run() {

        try {
            if (state == SENDING) {
                // 写入通道，这里可以看到没有对 byteBuffer 做任何处理，这里不需要，因为只是简单的回显而已
                channel.write(byteBuffer);
                // 写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                // 写完后,注册 read 就绪事件
                // **注意：这里并没有像创建handler时一样进行 select.wakeup()，是因为目前模型是单线程在跑，跑到这里的时候select()这个方法并没有在执行，这里注册了，到下一次select的时候是可以感知到的
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后,进入接收的状态
                state = RECIEVING;
                System.out.println("数据回发完毕!");
            } else if (state == RECIEVING) {
                // 从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    System.out.printf("收到数据: %s%n", new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8));
                }
                // 读完后，准备开始写入通道, byteBuffer切换成读模式
                byteBuffer.flip();
                // 读取数据后,立马注册写事件,主要是现在这个程序的功能主要用于回显,读取到数据之后立马准备返回去,注册写事件也是为了同一时间的其他读事件不干扰
                // **注意：这里并没有像创建handler时一样进行 select.wakeup()，是因为目前模型是单线程在跑，跑到这里的时候select()这个方法并没有在执行，这里注册了，到下一次select的时候是可以感知到的
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
        }
    }


}

