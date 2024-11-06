package org.hulei.chat.nio.single;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;

/**
 * @author hulei
 * @since 2024/9/13 20:02
 */

@SuppressWarnings({"resource"})
public class SingleReactorClient {

    @SneakyThrows
    public static void main(String[] args) {

        Selector selector = Selector.open();
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress("127.0.0.1", 8201));
        sc.register(selector, SelectionKey.OP_READ);

        while (!sc.finishConnect()) {

        }

        new Thread(new Runnable() {

            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            @SneakyThrows
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    selector.select();
                    Set<SelectionKey> sks = selector.selectedKeys();
                    for (SelectionKey sk : sks) {
                        int length = 0;
                        while ((length = ((SocketChannel) sk.channel()).read(buffer)) > 0) {
                            buffer.flip();
                            System.out.printf("server msg: %s%n", new String(buffer.array(), 0, length));
                            buffer.clear();
                        }
                    }
                    // 清空本次的 keys
                    sks.clear();
                }
            }
        }).start();

        final ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println("输入内容: ");
        while (!Thread.currentThread().isInterrupted()) {
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            buffer.put(msg.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            sc.write(buffer);
            buffer.clear();
        }
    }
}
