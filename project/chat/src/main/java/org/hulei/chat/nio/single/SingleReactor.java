package org.hulei.chat.nio.single;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * Reactor的职责主要是: 负责IO请求的检测,只要有IO事件,那么就发送给这个IO事件绑定的对应的handler
 *
 * @author hulei
 * @since 2024/9/13 17:38
 */

@SuppressWarnings("resource")
public class SingleReactor {

    public static Set<SocketChannel> channels = new HashSet<>();

    @SneakyThrows
    public static void main(String[] args) {
        // 先创建一个选择器,选择器的作用就是用来查询IO事件是否就绪
        Selector selector = Selector.open();
        // 在NIO中,连接都用通道来表示,数据的传输都是通过channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 设置监听的端口,非阻塞,开始监听
        ssChannel.configureBlocking(false);
        ssChannel.socket().bind(new InetSocketAddress("127.0.0.1", 8201));
        System.out.println("准备接收连接请求");

        // 第一步,注册一个accept事件,这个事件用于获取连接请求
        SelectionKey selectionKey = ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 预先埋点如果IO事件触发的情况下,应该执行什么操作,方便后续attachment拿出来直接执行
        selectionKey.attach((Runnable) () -> {
            try {
                // 这里直接获取新连接好了,因为我们只注册了连接事件
                SocketChannel accept = ssChannel.accept();
                System.out.printf("有新的连接进来, socket: %s%n", accept.socket().getPort());
                SingleHandler.createHandle(selector, accept, channels);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // 一直循环的进行事件查询
        while (!Thread.interrupted()) {
            // 这是一个阻塞请求,只有在确定有IO事件准备好之后,才会出发
            selector.select();
            for (SelectionKey selectedKey : selector.selectedKeys()) {
                // 这里reactor负责分发这些具体的IO事件对于reactor来说,他其实不关心具体的IO事件是什么
                // 他只负责把这个IO事件分发下去,具体什么操作,由最初绑定时的逻辑决定
                // 实现的最关键的操作就是 selectedKey.attachment 操作,允许我们拿到绑定事件的时候的处理对象
                Runnable attachment = (Runnable) selectedKey.attachment();
                attachment.run();
            }
            // 处理完之后要进行清空,避免下一次触发IO事件的时候还带有上一次的 key
            selector.selectedKeys().clear();
        }
    }
}
