package org.hulei.chat.nio.single;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Handler作为业务逻辑的主要处理类
 *
 * @author hulei
 * @since 2024/9/13 17:57
 */

@SuppressWarnings({"resource", "CallToPrintStackTrace"})
public class SingleHandler {

    @SneakyThrows
    public static void createHandle(Selector selector, SocketChannel isc, Set<SocketChannel> channels) {
        // 非阻塞式的连接
        isc.configureBlocking(false);
        // 先注册到选择器上,但是不注册任何事件,我们还需要做一些其他操作
        SelectionKey selectionKey = isc.register(selector, 0);
        // 这里 attach 具体的处理逻辑,作为聊天室,这里应该做的是把消息发给所有人
        selectionKey.attach(new Runnable() {
            /**
             * 使用一个共享的缓冲池
             */
            final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            @SneakyThrows
            @Override
            public void run() {

                // 这里的触发条件是读事件准备就绪
                try {
                    // 获取数据
                    int length = 0;
                    while ((length = isc.read(byteBuffer)) > 0) {
                        // 反转一下,成读模式,马上要发给其他所有的用户了
                        String msg = new String(byteBuffer.array(), 0, length, StandardCharsets.UTF_8);
                        System.out.println("收到消息为: " + msg);
                        byteBuffer.flip();
                        channels.forEach(new Consumer<SocketChannel>() {
                            @SneakyThrows
                            @Override
                            public void accept(SocketChannel osc) {
                                /*
                                在传输很长一段文本的时候(这里是超过1024个字节),那么会出现问题,就是超过了我们定义的byte的长度,或者说tcp主动分包的,这都会导致我们解析包的时候出现问题
                                所以定义协议的重要性在这里就体现出来了
                                 */
                                // 执行到这里的时候已经拿到的读事件,那么我们需要从信道中获取信息
                                byte[] bytes = String.format("%s - %d-%s", msg, isc.socket().getPort(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())).getBytes(StandardCharsets.UTF_8);
                                int length = bytes.length;
                                int start = 0;
                                int size = Math.min(1024, length);
                                while (start < length) {
                                    byteBuffer.clear();
                                    byteBuffer.put(bytes, start, size - start);
                                    start = size;
                                    size = Math.min(size + 1024, length);
                                    byteBuffer.flip();
                                    osc.write(byteBuffer);
                                }
                                byteBuffer.clear();
                            }
                        });
                    }
                    if (length == -1) {
                        System.out.printf("socket(%s)断开连接.", isc.socket().getPort());
                        selectionKey.cancel();
                        isc.finishConnect();
                        channels.remove(isc);
                    }
                } catch (IOException e) {
                    System.out.println("连接可能已经断开, 需要处理一下");
                    e.printStackTrace();
                    selectionKey.cancel();
                    isc.finishConnect();
                    channels.remove(isc);
                }
            }
        });
        selectionKey.interestOps(SelectionKey.OP_READ);
        channels.add(isc);
        System.out.println("singleHandler创建成功, 源socket: " + isc.socket().getPort());
    }
}
