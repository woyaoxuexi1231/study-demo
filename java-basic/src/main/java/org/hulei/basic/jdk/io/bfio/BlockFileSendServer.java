package org.hulei.basic.jdk.io.bfio;

import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 结合 BlockFileReceiveServer 类完成一次网络环境下的文件传输
 * 1. DataOutputStream 包装 socket 的 os，更加方便的发送文件大小和文件名
 * 2. FileInputStream 使用字节流读取文件并通过 dos 传送给 BlockFileReceiveServer
 *
 * @author hulei
 * @since 2024/9/13 12:13
 */


@SuppressWarnings({"resource"})
public class BlockFileSendServer {
    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8101)) {

            //============================================ 获取文件 ============================================
            // 读取 classes 目录下 system.properties 文件
            String sourcePath = "/system.properties";
            String srcPath = NIOUtil.getResourcePath(sourcePath);
            System.out.printf("文件路径 = %s", srcPath);
            File file = new File(srcPath);
            if (file.exists()) {

                //============================================ 构建 io 流 ============================================
                // 文件的输入输出流
                FileInputStream fis = new FileInputStream(file);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());


                //============================================ 按照定义的格式发送内容 ============================================
                /*
                这里发送数据和客户端形成 成对关系，可以看到这里 flush 之后，在客户端有一次 read 操作
                在服务器这里 flush 之前，客户端的 read 都会阻塞，因为客户端的缓冲区没东西
                 */
                // 文件长度
                dos.writeLong(file.length());
                dos.flush();

                // 这里如果暂停10秒，客户端那里会阻塞3秒钟
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(3));

                // 想让服务端保存的文件名
                dos.writeUTF("block_file_send_and_receive_" + file.getName());
                dos.flush();

                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    System.out.printf("| %s%% |", (100 * progress / file.length()));
                }

                System.out.println("======== 文件传输成功 ========");
            } else {
                System.out.println("======== 文件传输失败, 文件不存在 ========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
