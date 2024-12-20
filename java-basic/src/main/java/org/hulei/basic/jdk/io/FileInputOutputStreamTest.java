package org.hulei.basic.jdk.io;

import com.github.jsonzou.jmockdata.JMockData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hulei
 * @since 2024/9/11 18:23
 */

public class FileInputOutputStreamTest {

    private static final String fileName = "test2.txt";

    @SneakyThrows
    public static void main(String[] args) {

        // fileTest();
        fileOutputStream();
        fileInputStream();

        // bufferedOutputStream();
        // bufferedInputStream();

        // dataOutputStream();
        // dataInputStream();

        // objectOutputStream();
        // objectInputStream();

        // outputStreamWriter();
        // fileInputStreamReader();


    }

    /**
     * 使用 File 操作文件/文件夹
     * File 主要只能用来操作文件/文件夹,但是并不能读取和吸入文件内容
     */
    @SneakyThrows
    private static void fileTest() {
        File file = new File(fileName);
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.printf("文件 %s 创建成功%n", fileName);
            }
        } else {
            System.out.printf("文件 %s 已经存在%n", fileName);
        }
    }


    /* ===================================================字节流================================================ */

    /**
     * 使用 FileInputStream 读取文件的内容
     * FileInputStream只能从文件中按照顺序读取原始字节数据(也就是一个字节一个字节的来读,数据按照 ascii 的编码规则出来)
     * 比如 你 这个字, UTF-8 编码下由3个字节 0xE4 0xBD 0xA0 表示, FileInputStream每次读取的只是单个字节。这时，我们会得到 3 个不同的字节
     * ascii码 一共255位 2^8 正好一个字节
     */
    @SneakyThrows
    private static void fileInputStream() {
        StringBuilder sb;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            int content;
            sb = new StringBuilder();
            // fis.read() 获取的是文本的 ascii 码
            while ((content = fis.read()) != -1) {
                sb.append(content).append(" ");
            }
        }
        System.out.println(sb);
    }

    /**
     * 使用 FileOutputStream 写入文件内容
     */
    @SneakyThrows
    private static void fileOutputStream() {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            byte[] bytes = "你".getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
        }
    }

    /**
     * BufferedInputStream 读取文件
     * BufferedInputStream 是 FilterInputStream 的子类,为输入流添加缓冲,提高读取效率
     * 但是仍然属于字节流读取
     */
    @SneakyThrows
    private static void bufferedInputStream() {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            int data;
            StringBuilder sb = new StringBuilder();
            while ((data = bis.read()) != -1) {
                sb.append(data).append(" ");
            }
            System.out.println(sb);
        }
    }

    @SneakyThrows
    private static void bufferedOutputStream() {
        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            String s = "hello world";
            bos.write(s.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 以 DataInputStream 读取文件内容, 和 DataOutputStream 配套使用
     */
    @SneakyThrows
    private static void dataInputStream() {
        try (DataInputStream dis = new DataInputStream(Files.newInputStream(Paths.get(fileName)))) {
            // 读取文件中的数据
            int intValue = dis.readInt();
            double doubleValue = dis.readDouble();
            boolean booleanValue = dis.readBoolean();
            char charValue = dis.readChar();

            // 打印读取的数据
            System.out.println("读取的整数值: " + intValue);
            System.out.println("读取的双精度值: " + doubleValue);
            System.out.println("读取的布尔值: " + booleanValue);
            System.out.println("读取的字符值: " + charValue);

        }
    }

    /**
     * 使用 dataOutputStream 输出内容到文件内
     * 写入的内容实际基于 Java 基本类型的规范来确定: 1-整形类型,以四个字符存储 2-布尔类型,以一个字符存储 3-char,占两个字节
     * 并不是以特定的字符集来确定的
     */
    @SneakyThrows
    private static void dataOutputStream() {
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            dos.writeInt(10);
            dos.writeDouble(12.3);
            dos.writeBoolean(true);
            dos.writeChar('你');
        }
    }

    /**
     * 以 ObjectInputStream 的形式读取文件内容,配套 ObjectOutputStream 使用
     * 对象需要实现 Serializable 接口以实现对象的序列化和反序列化
     */
    @SneakyThrows
    private static void objectInputStream() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
            System.out.println(ois.readObject());
            System.out.println(ois.readObject());
        }
    }

    /**
     * 以 ObjectOutputStream 写入文件内容
     */
    @SneakyThrows
    private static void objectOutputStream() {
        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)));
        oos.writeObject(new SimpleObject(JMockData.mock(String.class)));
        oos.writeObject(new SimpleObject(JMockData.mock(String.class)));
    }

    @AllArgsConstructor
    @Data
    static class SimpleObject implements Serializable {
        private String value;
    }

    /* ===================================================字符流================================================ */

    /**
     * 使用 InputStreamReader 读取文件内容
     * InputStreamReader 是一个字符流，它的核心功能是将字节流（如 FileInputStream）转换为字符流。
     * 在这个过程中，InputStreamReader 使用指定的字符编码（如 UTF-8 或 GBK）来解码字节，将这些字节组合起来并转换为对应的字符。
     * 当 InputStreamReader 遇到字节 0xE4 0xBD 0xA0 时，它知道根据 UTF-8 编码，这三个字节一起表示的是中文字符“你”，因此可以正确地将其解码并输出。
     * <p>
     * 字符编码有明确的规定,定义每个字符需要多少个字节来表示.
     * 对于UTF-8这种可变长度的编码,他的字节前缀会根据字符具体的长度有所不同. 比如 0xxxxxxx 1字节, 110xxxxx 2字节
     */
    @SneakyThrows
    private static void inputStreamReader() {
        // 替换为你的文件路径
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        // 指定字符编码
        int character;
        StringBuilder sb = new StringBuilder();
        while ((character = isr.read()) != -1) {
            sb.append((char) character);
        }
        System.out.println(sb);
    }

    @SneakyThrows
    private static void outputStreamWriter(){
        OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(Paths.get(fileName)));
        osw.write("hello world\n");
        osw.write("hello world");
        osw.flush();
    }

}
