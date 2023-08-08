package com.crazymakercircle.ReactorModel;


import com.crazymakercircle.util.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

class EchoHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;

    EchoHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        c.configureBlocking(false);
        //仅仅取得选择键, 后设置感兴趣的IO事件, 这里 ops = 0 表示不对任何事件感兴趣
        sk = channel.register(selector, 0);

        //将 EchoHandler 本身作为选择键的附件
        sk.attach(this);

        //第二步, 注册 Read 就绪事件
        sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        /*
        1. 当 B 线程阻塞在 select() 或 select(long) 方法上时, A线程调用 wakeup 后, B线程会立刻返回
        2. 如果没有线程阻塞在 select() 方法上, 那么下一次某个线程调用 select() 或 select(long) 方法时, 会立刻返回.
        所以, 当在 Handler 中完成 SocketChannel 注册后, 显示的调用 selector.wakeup() 方法, 虽然当前没有阻塞在 select() 上, 但是会影响下一次调用 select().
        作者：topgunviper
        链接：https://www.zhihu.com/question/389599681/answer/1176335201
        来源：知乎
        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
         */
        selector.wakeup();
    }

    public void run() {

        try {

            if (state == SENDING) {
                // //写入通道
                channel.write(byteBuffer);
                //写完后, 准备开始从通道读, byteBuffer切换成写模式
                byteBuffer.clear();
                //写完后, 注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                //写完后, 进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                // 通道内有数据了, 开始读取
                int length;
                // 把通道内的数据写入 byteBuffer, 此时 byteBuffer 是写模式
                while ((length = channel.read(byteBuffer)) > 0) {
                    Logger.info(new String(byteBuffer.array(), 0, length));
                }
                //读完后, 准备开始写入通道, byteBuffer 切换成读模式
                byteBuffer.flip();
                //读完后, 注册 write 就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //读完后, 进入发送的状态
                state = SENDING;
            }
            //处理结束了, 这里不能关闭 select key, 需要重复使用
            /*
            SelectionKey.cancel() 是一个方法, 它用于取消选择键的注册. 每当一个通道向选择器注册时, 都会创建一个选择键
            选择键在以下情况下保持有效: 通过调用其 cancel 方法取消, 通过关闭其通道, 或通过关闭其选择器
            取消选择键不会立即从选择器中移除它: 它会被添加到选择器的已取消键集中，在下一次 select 操作期间移除, 可以使用 SelectionKey.isValid() 方法来测试键的有效性
             */
            //sk.cancel();
        } catch (Exception ex) {
            ex.printStackTrace();
            sk.cancel();
            try {
                channel.finishConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

