package org.hulei.jdk.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author hulei
 * @since 2024/9/13 12:14
 */

@SuppressWarnings({"resource"})
@Slf4j
public class NIOStreamCopyFile {

    /**
     * 这里并没有真正的使用 NIO,仅仅也只是使用了 channel 和 buffer
     * 使用 NIO 的 Channel,Buffer 来实现文件的复制操作
     */
    @SneakyThrows
    public static void main(String[] args) {

        NIOUtil.createDir();

        String resPath = NIOUtil.getResourcePath("/system.properties");
        String desPath = NIOUtil.getResourcePath("/upload/nio_buffer_copy_file_system.properties");

        File srcFile = new File(resPath);
        File desFile = new File(desPath);

        long startTime = System.currentTimeMillis();

        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(desFile);

        FileChannel inputChannel = input.getChannel();
        FileChannel outputChannel = output.getChannel();

        // 分配 buffer 缓冲区的初始化大小
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        // 从输入通道(inputChannel)读取数据输入到 buffer缓冲区
        while (inputChannel.read(buf) != -1) {
            // 翻转 buf, 变成成读模式
            buf.flip();
            int outlength;
            // 将 buf 写入到输出的通道
            while ((outlength = outputChannel.write(buf)) != 0) {
                log.info("写入字节数：{}", outlength);
            }
            // 清除 buf, 变成写入模式, 以便下一次从 inputChannel 读取数据写入 buffer
            buf.clear();
        }

        // 强制刷新磁盘
        outputChannel.force(true);
    }
}
