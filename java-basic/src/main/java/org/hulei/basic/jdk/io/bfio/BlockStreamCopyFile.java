package org.hulei.basic.jdk.io.bfio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 使用 FileInputStream 和 FileOutputStream 完成一次文件的复制
 * @author hulei
 * @since 2024/9/13 12:14
 */

public class BlockStreamCopyFile {

    @SneakyThrows
    public static void main(String[] args) {

        // 先创建一个 upload 文件夹
        NIOUtil.createDir();

        String resPath = NIOUtil.getResourcePath("/system.properties");
        String desPath = NIOUtil.getResourcePath("/upload/block_stream_copy_system.properties");

        File srcFile = new File(resPath);
        File desFile = new File(desPath);

        long startTime = System.currentTimeMillis();

        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(desFile);

        // 输入输出流都通过这一个buffer来进行文件的复制操作,每次读1024个字节出来
        byte[] buf = new byte[1024];
        int len = 0;
        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            // 每次最多只能读取1024个字节,这里顺便统计一下文件大小
            len += bytesRead;
            output.write(buf, 0, bytesRead);
        }
        output.flush();
        long endTime = System.currentTimeMillis();
        System.out.printf("IO流复制毫秒数：%s, 文件大小一共: %d%n", endTime - startTime, len);
    }
}
