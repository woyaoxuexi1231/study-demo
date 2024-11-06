package org.hulei.chat.bio.server;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hulei
 * @since 2024/9/13 17:02
 */

@SuppressWarnings("resource")
public class Server {

    static Set<Socket> sockets = new HashSet<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8200);
            while (!Thread.interrupted()) {
                // 收到客户端的请求
                Socket accept = serverSocket.accept();
                System.out.println("Accepted connection from " + accept.getPort());

                // 收到的客户端请求放入已打开的连接的集合
                sockets.add(accept);

                // 对每个存在的连接都新建一个线程来处理
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        // 构建输入输出流
                        InputStream is = accept.getInputStream();
                        DataInputStream dis = new DataInputStream(is);

                        // 读取来自客户端的消息
                        byte[] bytes = new byte[1024];
                        int length = 0;
                        try {
                            while ((length = dis.read(bytes)) != -1) {

                                String msg = new String(Arrays.copyOf(bytes, length), StandardCharsets.UTF_8);
                                System.out.println("Server: " + msg);
                                // 对于发消息的操作,需要保证异常不被抛出
                                sockets.forEach(socket -> {
                                    try {
                                        OutputStream os = socket.getOutputStream();
                                        os.write(String.format("%s - %d-%s", msg, socket.getPort(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())).getBytes(StandardCharsets.UTF_8));
                                    } catch (Exception e) {
                                        System.out.println("发送消息出现了异常,但是不处理");
                                    }
                                });
                            }
                            // 当读取的结果为-1,代表客户端主动断开了连接
                            sockets.remove(accept);
                        } catch (SocketException e) {
                            // 如果非正常断开,那么read可能会报错,这样需要把当前连接断开
                            System.out.printf("有socket断开连接, socket: %s, error msg: %s %n", accept.getPort(), e.getMessage());
                            // 从set集合中移除用户
                            sockets.remove(accept);
                        }
                        // int operation = dis.readInt();
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
