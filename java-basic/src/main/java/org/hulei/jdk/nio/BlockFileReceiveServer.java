package org.hulei.jdk.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.jdk.jvm.JvmUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2024/9/13 12:11
 */

@SuppressWarnings({"resource"})
@Slf4j
public class BlockFileReceiveServer {

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocket ss = new ServerSocket(8101)) {

            log.info("启动服务器中,准备接受客户端连接...");
            Socket socket = ss.accept();
            try {

                // 用 DataInputStream 来按照固定的格式来读取我们的数据
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // 读取文件长度和文件名,在发送端也是按照这个格式来发送的
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                NIOUtil.createDir();

                // 确定上传的文件的文件名
                File file = new File(NIOUtil.getResourcePath("/upload" + File.separatorChar + fileName));
                FileOutputStream fos = new FileOutputStream(file);

                // 开始读取发送方发送的具体文件内容
                long startTime = System.currentTimeMillis();
                log.info("block IO 传输开始：");

                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                    // LockSupport.parkNanos(1000 * 1000 * 1000 * 60L);
                }

                log.info("文件接收成功,File Name：{}", fileName);
                log.info(" Size：{}", NIOUtil.getFormatFileSize(fileLength));
                long endTime = System.currentTimeMillis();
                log.info("block IO 传输毫秒数：{}", endTime - startTime);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
