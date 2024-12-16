package org.hulei.chat.bio.client;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author hulei
 * @since 2024/9/13 17:11
 */

@SuppressWarnings("resource")
public class Client {

    @SneakyThrows
    public static void main(String[] args) {

        // 创建新的客户端连接,并把这个连接放入一个新的线程,这个线程一直读取服务器发来的消息
        Socket socket = new Socket("127.0.0.1", 8105);
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                InputStream is = socket.getInputStream();
                byte[] buf = new byte[1024];
                int length = 0;
                while ((length = is.read(buf)) != -1) {
                    System.out.println("收到消息: " + new String(Arrays.copyOf(buf, length), StandardCharsets.UTF_8));
                }
            }
        }).start();

        // 一直从输入中读取数据发送给客户端
        Scanner scanner = new Scanner(System.in);
        while (!Thread.interrupted()) {
            System.out.println("输入内容: ");
            String msg = scanner.nextLine();
            if ("exit".equals(msg)) {
                socket.close();
                break;
            }
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(String.format("%s", msg).getBytes(StandardCharsets.UTF_8));
        }
    }
}
