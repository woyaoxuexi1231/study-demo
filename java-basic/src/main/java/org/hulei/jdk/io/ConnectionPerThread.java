package org.hulei.jdk.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 每个新的网络连接都分配一个专门的线程负责IO处理,每个线程都独自处理各自负责的 Socket 连接的输入和输出
 * 不过这个模型有一个本质的问题在于严重依赖线程
 * 1. 线程的创建和销毁成本是很高的,都需要通过重量级的系统调用去完成
 * 2. 线程本身占用的内存较大,像Java线程的栈内存,一般至少分配512kb~1MB的空间,如果一个Java进程创建了超过一千个线程,整个jvm的内存将会消耗1GB以上
 * 3. 线程的切换成本很高,操作系统发生线程切换的时候,需要保留线程的上下文,然后执行系统调用
 * 4. 容易造成锯齿状的系统负载
 *
 * @author hulei
 * @since 2024/9/13 12:07
 */

@Slf4j
public class ConnectionPerThread {

    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(8100)) {

            System.out.println("启动服务器中,准备接受客户端连接...");
            while (!Thread.interrupted()) {
                // 接受一个客户端的连接,这个方法是一个阻塞方法,在没有收到新的连接之前,这个方法会一直阻塞
                Socket s = ss.accept();
                new Thread(() -> {
                    try {
                        System.out.println("客户端:" + s.getInetAddress() + "已连接到服务器");
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        String mess;
                        // br.readLine()这个方法是一个阻塞的方法,在都缓冲区没有检测到数据的情况下是不会返回的
                        // 在客户端采用一种正确的关闭socket的方法的话,这里是不会报错的,如果直接杀掉客户端,会报错 java.net.SocketException: Connection reset
                        while ((mess = br.readLine()) != null) {
                            System.out.printf("客户端(端口 %s)：%s%n", s.getPort(), mess);
                            bw.write("服务器收到: " + mess + "\n");
                            bw.flush();
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }, "connection_per_thread").start();
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
