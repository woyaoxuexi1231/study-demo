package org.hulei.basic.jdk.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.basic.jdk.jvm.JvmUtil;

import java.io.File;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

/**
 * @author hulei
 * @since 2024/9/11 15:55
 */

@Slf4j
public class NIOUtil {

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    public static class Session {

        public int step = 1; // 1 读取文件名称的长度，2 读取文件名称  ，3 ，读取文件内容的长度， 4 读取文件内容

        // 文件名长度
        public int fileNameLength;
        // 文件名称
        public String fileName = null;
        // 长度
        public long fileLength;
        // 开始传输的时间
        public long startTime;
        // 客户端的地址
        public InetSocketAddress remoteAddress;
        // 输出的文件通道
        public FileChannel fileChannel;
        // 接收长度
        public long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }

    public static int len(ByteBuffer buffer) {
        // log.info(" >>>  buffer left：{}", buffer.remaining());
        return buffer.remaining();
    }

    private static final DecimalFormat fileSizeFormater = decimalFormat(1);

    /**
     * 设置数字格式，保留有效小数位数为fractions
     *
     * @param fractions 保留有效小数位数
     * @return 数字格式
     */
    public static DecimalFormat decimalFormat(int fractions) {

        DecimalFormat df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(fractions);
        df.setMaximumFractionDigits(fractions);
        return df;
    }

    /**
     * 格式化文件大小
     *
     * @param length 文件大小
     * @return 格式化后的文件大小字符串
     */
    public static String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if (size >= 1) {
            return fileSizeFormater.format(size) + "GB";
        }
        size = ((double) length) / (1 << 20);
        if (size >= 1) {
            return fileSizeFormater.format(size) + "MB";
        }
        size = ((double) length) / (1 << 10);
        if (size >= 1) {
            return fileSizeFormater.format(size) + "KB";
        }
        return length + "B";
    }

    /**
     * 取得当前类路径下的 resName 资源的完整路径
     * url.getPath() 获取到的路径被 utf-8 编码了,需要用URLDecoder.decode(path, "UTF-8")解码
     *
     * @param resName 需要获取完整路径的资源,需要以/打头
     * @return 完整路径
     */
    @SneakyThrows
    public static String getResourcePath(String resName) {

        // 在路径下查找指定指定的文件, 如果给定的路径以 / 开头,那么代表从类的根路径开始查找,在这里也就是 target/classes 目录下开始查找, 如果不以 / 开头,那么从这个类的同级目录下查找
        // 这里尝试通过文件名来获取文件的绝对路径
        URL url = NIOUtil.class.getResource(resName);
        String path;
        if (null == url) {
            path = NIOUtil.class.getResource("/").getPath() + resName;
        } else {
            path = url.getPath();
        }
        String decodePath = URLDecoder.decode(path, StandardCharsets.UTF_8);
        // 如果是 Windows 系统, 路径前缀会有一个 /,这里删除这个无效的 /
        if (JvmUtil.isWin()) {
            return decodePath.substring(1);
        }
        return decodePath;
    }

    public static void createDir() {
        // 确定上传目录,以及保存内容的文件 NIOTest.class.getResource("/")可以得到当前类的根目录的绝对路径
        String dirPath = JvmUtil.isWin() ? NIOUtil.class.getResource("/").getPath().substring(1) + "upload" : NIOUtil.class.getResource("/").getPath() + "upload";
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }


    /**
     * 带着线程名+类名+方法名称输出
     *
     * @param s 待输出的字符串形参
     */
    synchronized public static void info(Object s) {
        String content = null;
        if (null != s) {
            content = s.toString().trim();
        } else {
            content = "";
        }
        String cft = "[" + Thread.currentThread().getName() + "|" + getNakeCallClassMethod() + "]" + " time: " + LocalDateTime.now();

        String out = String.format("%20s |>  %s ", cft, content);
        System.out.println(out);

    }

    /**
     * 获得调用方法的类名+方法名
     *
     * @return 方法名称
     */
    public static String getNakeCallClassMethod() {
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();
        // 获得调用方法名
        String[] className = stack[3].getClassName().split("\\.");
        String fullName = className[className.length - 1] + "." + stack[3].getMethodName();
        return fullName;
    }
}
