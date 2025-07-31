package org.hulei.basic.jdk.io.nfio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.io.NIOUtil;

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
public class NIOStreamCopyFile {

    /**
     * 这里并没有真正的使用 NIO,仅仅也只是使用了 channel 和 buffer
     * 使用 NIO 的 Channel,Buffer 来实现文件的复制操作
     * 虽然整个过程看着比BIO的过程还复杂
     */
    @SneakyThrows
    public static void main(String[] args) {

        // ========================================== 源文件和目标文件 =============================================
        NIOUtil.createDir();
        String resPath = NIOUtil.getResourcePath("/system.properties");
        String desPath = NIOUtil.getResourcePath("/upload/nio_buffer_copy_file_system.properties");
        File srcFile = new File(resPath);
        File desFile = new File(desPath);
        long startTime = System.currentTimeMillis();


        // ========================================== 声明 FileChannel =============================================
        /*
        FileChannel 专门用于操作文件的通道，通过FileChannel既可以从文件中读取数据，也可以将数据写入文件中
        FileChannel 只能设置为阻塞模式，不能设置为非阻塞模式。
         */
        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(desFile);
        FileChannel inputChannel = input.getChannel();
        FileChannel outputChannel = output.getChannel();
        // 分配 buffer 缓冲区的初始化大小
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);

        // ========================================== 开始复制 =============================================
        // 从输入通道(inputChannel)读取数据输入到 buffer缓冲区
        while (inputChannel.read(buf) != -1) {
            // 翻转 buf, 变成成读模式
            buf.flip();
            int outlength;
            // 将 buf 写入到输出的通道
            while ((outlength = outputChannel.write(buf)) != 0) {
                System.out.printf("写入字节数：%s%n", outlength);
            }
            // 清除 buf, 变成写入模式, 以便下一次从 inputChannel 读取数据写入 buffer
            buf.clear();
        }

        // 强制刷新磁盘
        outputChannel.force(true);
    }
}
