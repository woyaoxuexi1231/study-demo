package org.hulei.basic.jdk.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author hulei
 * @since 2024/9/13 12:07
 */

@Slf4j
public class ConnectionPerThread {

    public static void main(String[] args) {

        // 服务器端监听 8100 端口
        try (ServerSocket ss = new ServerSocket(8100)) {
            System.out.println("启动服务器中,准备接受客户端连接...");
            // 只要当前的主线程没有中断，就一直接受客户端的连接请求
            while (!Thread.interrupted()) {
                // 接受一个客户端的连接,这个方法是一个阻塞方法,在没有收到新的连接之前,这个方法会一直阻塞
                Socket s = ss.accept();
                /*
                每个新的网络连接都分配一个专门的线程负责IO处理,每个线程都独自处理各自负责的 Socket 连接的输入和输出
                但是这样做也带来了巨大的代价：
                    1. 线程的创建和销毁成本是很高的,都需要通过重量级的系统调用去完成
                    2. 线程本身占用的内存较大,像Java线程的栈内存,一般至少分配512kb~1MB的空间,如果一个Java进程创建了超过一千个线程,整个jvm的内存将会消耗1GB以上
                    3. 线程的切换成本很高,操作系统发生线程切换的时候,需要保留线程的上下文,然后执行系统调用
                    4. 容易造成锯齿状的系统负载(指系统资源使用率（如CPU、内存、磁盘I/O或网络流量）在监控图表上呈现出规律性的上升和下降，形成类似锯齿的模式。这种模式通常表明系统存在周期性的负载波动。)
                 */
                new Thread(() -> {
                    try {
                        System.out.println("客户端:" + s.getInetAddress() + "已连接到服务器");
                        /*
                        InputStream 和 OutputStream 可以读写单个字节或者是字节数组
                        DataInputStream 和 OutputStream 可以以二进制格式读写所有的Java基本类型

                        对于 Unicode 文本来说，Reader 和 Buffer 是更好的选择，自动处理字符编码的转换。

                        应使用字节流的场景：
                            1.图像、音频、视频等媒体文件
                            2.网络数据传输
                            3.序列化对象
                            4.ZIP等压缩文件
                        应使用字符流的场景：
                            1.文本文件(.txt, .csv等)
                            2.XML/JSON处理
                            3.控制台输入输出
                            4.字符串处理

                        而这里客户端和服务器回复都采用字符串，所以使用 Reader 和 Writer 会更加方便，省去了使用字节流的转码过程
                        在 BlockFileSendServer BlockFileReceiveServer 使用字节流来传输文件，在传输文件时不关心编码问题，所以使用字节流更合适，而且更快
                         */
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        String mess;
                        /*
                        br.readLine()这个方法是一个阻塞的方法,在都缓冲区没有检测到数据的情况下是不会返回的
                        在客户端采用一种正确的关闭socket的方法的话,这里是不会报错的,如果直接杀掉客户端,会报错 java.net.SocketException: Connection reset
                         */
                        while ((mess = br.readLine()) != null) {
                            System.out.printf("客户端(端口 %s)：%s%n", s.getPort(), mess);
                            Thread.sleep(5000);
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
