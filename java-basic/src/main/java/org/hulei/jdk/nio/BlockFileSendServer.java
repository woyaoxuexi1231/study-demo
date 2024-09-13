package org.hulei.jdk.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @author hulei
 * @since 2024/9/13 12:13
 */


@SuppressWarnings({"resource"})
@Slf4j
public class BlockFileSendServer {
    public static void main(String[] args) {

        try (Socket socket = new Socket("127.0.0.1", 8101)) {

            // 读取 classes 目录下 system.properties 文件
            String sourcePath = "/system.properties";
            String srcPath = NIOUtil.getResourcePath(sourcePath);
            log.info("文件路径 = {}", srcPath);

            File file = new File(srcPath);
            if (file.exists()) {

                // 文件的输入输出流
                FileInputStream fis = new FileInputStream(file);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // 写入文件的长度
                dos.writeLong(file.length());
                dos.flush();
                // 想让服务端保存的文件名
                dos.writeUTF("block_file_send_and_receive_" + file.getName());
                dos.flush();

                // 开始传输文件
                log.info("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    log.info("| {}% |", (100 * progress / file.length()));
                }

                log.info("======== 文件传输成功 ========");
            } else {
                log.info("======== 文件传输失败, 文件不存在 ========");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
