package org.hulei.jdk.nio.reactor;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("CallToPrintStackTrace")
@Slf4j
class EchoHandler implements Runnable {

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    static final int RECIEVING = 0, SENDING = 1;
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
        // TODO 这个作用是啥?
        selector.wakeup();
    }

    @Override
    public void run() {

        try {

            if (state == SENDING) {
                // 写入通道
                channel.write(byteBuffer);
                log.info("写入数据 {}", new String(byteBuffer.array(), StandardCharsets.UTF_8));
                // 写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                // 写完后,注册read就绪事件
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
                // 读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                // 读完后,进入发送的状态
                state = SENDING;
            }
            // 处理结束了, 这里不能关闭select key，需要重复使用
            // sk.cancel();
        } catch (IOException ex) {
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

