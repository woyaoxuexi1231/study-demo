package org.hulei.basic.jdk.io.bfio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 结合 BlockFileSendServer 完成一次网络环境的文件接收
 * DataInputStream 和 FileOutputStream 的使用和 BlockFileSendServer 类似
 *
 * @author hulei
 * @since 2024/9/13 12:11
 */

@SuppressWarnings({"resource"})
public class BlockFileReceiveServer {

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocket ss = new ServerSocket(8101)) {

            System.out.println("启动服务器中,准备接受客户端连接...");
            Socket socket = ss.accept();
            try {

                //============================================ 文件名和文件大小的确定 ============================================
                // 用 DataInputStream 来按照固定的格式来读取我们的数据，DataInputStream 可以按照某种类型来获取数据
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // 这里读取内容时发送方和接收方都预先进行数据的定义，规定先读什么，后读什么，这相当于可以认为是一个协议的雏形
                // 读取文件长度和文件名,在发送端也是按照这个格式来发送的
                long fileLength = dis.readLong();
                System.out.printf("文件长度：%s%n", fileLength);
                String fileName = dis.readUTF();
                System.out.printf("文件名：%s%n", fileName);

                // 这里先创建一个文件的保存目录
                NIOUtil.createDir();

                // 通过目录和文件名拼接一个文件的绝对路径
                File file = new File(NIOUtil.getResourcePath("/upload" + File.separatorChar + fileName));
                System.out.printf("文件将保存在 %s%n", file.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(file);


                //============================================ 开始读取文件的内容 ============================================
                // 开始读取发送方发送的具体文件内容
                long startTime = System.currentTimeMillis();
                System.out.println("block IO 传输开始：");

                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                    // LockSupport.parkNanos(1000 * 1000 * 1000 * 60L);
                }

                long endTime = System.currentTimeMillis();
                System.out.printf("文件接受成功，文件大小：%s, block IO 传输毫秒数：%s", NIOUtil.getFormatFileSize(fileLength), endTime - startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
