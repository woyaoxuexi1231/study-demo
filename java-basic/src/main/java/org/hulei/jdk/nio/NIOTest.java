package org.hulei.jdk.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 * @author hulei
 * @since 2024/9/11 15:55
 */

@Slf4j
public class NIOTest {

    public static void main(String[] args) {

        // connectionPerThread();
        // String sourcePath = "system.properties";
        // String srcPath = getResourcePath(sourcePath);
        // System.out.println(srcPath);

        // System.out.println(NIOTest.class.getResource("/system.properties"));

        // new Thread(NIOTest::blockReceiveServer, "blockReceiveServer").start();
        // new Thread(NIOTest::blockSendServer, "blockSendServer").start();

        // blockStreamCopyFile();
        // nioBufferCopyFile();

        // nioReceiveServer();
        nioSendServer();
    }

    /**
     * 每个新的网络连接都分配一个专门的线程负责IO处理,每个线程都独自处理各自负责的 Socket 连接的输入和输出
     * 不过这个模型有一个本质的问题在于严重依赖线程
     * 1. 线程的创建和销毁成本是很高的,都需要通过重量级的系统调用去完成
     * 2. 线程本身占用的内存较大,像Java线程的栈内存,一般至少分配512kb~1MB的空间,如果一个Java进程创建了超过一千个线程,整个jvm的内存将会消耗1GB以上
     * 3. 线程的切换成本很高,操作系统发生线程切换的时候,需要保留线程的上下文,然后执行系统调用
     * 4. 容易造成锯齿状的系统负载
     */
    public static void connectionPerThread() {
        try (ServerSocket ss = new ServerSocket(8888)) {
            System.out.println("启动服务器中,准备接受客户端连接...");
            while (!Thread.interrupted()) {
                // 接受一个客户端的连接,这个方法是一个阻塞方法,在没有收到新的连接之前,这个方法会一直阻塞
                Socket s = ss.accept();
                new Thread(() -> {
                    try {
                        System.out.println("客户端:" + s.getInetAddress() + "已连接到服务器");
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        String mess;
                        while ((mess = br.readLine()) != null) {
                            System.out.println("客户端：" + mess);
                            bw.write("服务器收到: " + mess + "\n");
                            bw.flush();
                        }
                    } catch (Exception e) {
                        log.error(" ", e);
                    }
                }, "bio-test-").start();
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 以阻塞IO的形式接受来自客户端的文件
     */
    @SneakyThrows
    public static void blockReceiveServer() {
        try (ServerSocket ss = new ServerSocket(8888)) {
            log.info("启动服务器中,准备接受客户端连接...");
            Socket socket = ss.accept();
            try {
                // 用 DataInputStream 来按照固定的格式来读取我们的数据
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // 读取文件长度和文件名,在发送端也是按照这个格式来发送的
                long fileLength = dis.readLong();
                String fileName = dis.readUTF();

                // 确定上传目录,以及保存内容的文件 NIOTest.class.getResource("/")可以得到当前类的根目录的绝对路径
                String dirPath = JvmUtil.isWin() ? NIOTest.class.getResource("/").getPath().substring(1) + "upload" : NIOTest.class.getResource("/").getPath() + "upload";
                File directory = new File(dirPath);
                if (!directory.exists()) {
                    directory.mkdir();
                }

                // 确定上传的文件的文件名
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                FileOutputStream fos = new FileOutputStream(file);

                // 开始读取发送方发送的具体文件内容
                long startTime = System.currentTimeMillis();
                log.info("block IO 传输开始：");
                // 开始接收文件,使用 DataInputStream 读取,那么发送方也只能发送 ascii 包含的字符才行,如果出现其他字符编码,会直接乱码
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }

                log.info("文件接收成功,File Name：{}", fileName);
                log.info(" Size：{}", getFormatFileSize(fileLength));
                long endTime = System.currentTimeMillis();
                log.info("block IO 传输毫秒数：{}", endTime - startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 以阻塞IO的形式发送一个文件
     */
    @SneakyThrows
    public static void blockSendServer() {

        try (Socket socket = new Socket("127.0.0.1", 8888)) {

            // 读取 classes 目录下 system.properties 文件
            String sourcePath = "/system.properties";
            String srcPath = getResourcePath(sourcePath);
            log.info("文件路径 = {}", srcPath);

            File file = new File(srcPath);
            if (file.exists()) {

                // 文件的输入输出流
                FileInputStream fis = new FileInputStream(file);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // 写入文件的长度
                dos.writeLong(file.length());
                dos.flush();
                // 写入文件的名字
                dos.writeUTF("copy_" + file.getName());
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
            e.printStackTrace();
        }
    }

    /**
     * 使用 FileInputStream 和 FileOutputStream 进行文件的复制
     */
    @SneakyThrows
    public static void blockStreamCopyFile() {

        String resPath = getResourcePath("/system.properties");
        String desPath = getResourcePath("/block_stream_system.properties");

        File srcFile = new File(resPath);
        File desFile = new File(desPath);

        long startTime = System.currentTimeMillis();

        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(desFile);

        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, bytesRead);
        }
        output.flush();
        long endTime = System.currentTimeMillis();
        log.info("IO流复制毫秒数：{}", endTime - startTime);

    }

    /**
     * 使用 NIO 的 Channel,Buffer 来实现文件的复制操作
     */
    @SneakyThrows
    public static void nioBufferCopyFile() {

        String resPath = getResourcePath("/system.properties");
        String desPath = getResourcePath("/nio_buffer_system.properties");

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

    @SneakyThrows
    public static void nioSendServer() {

        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try {

            // 发送小文件
            String srcPath = "/system.properties";
            // 发送一个大的
//            String srcPath = NioDemoConfig.SOCKET_SEND_BIG_FILE;
            File file = new File(srcPath);
            if (!file.exists()) {
                srcPath = getResourcePath(srcPath);
                file = new File(srcPath);
                if (!file.exists()) {
                    log.info("文件不存在");
                    return;
                }
            }

            // 输入通道
            fileChannel = new FileInputStream(file).getChannel();
            // 创建一个套接字传输通道
            socketChannel = SocketChannel.open();
            // 禁用Nagle算法。
            // Nagle算法 是为了解决网络上小包过多的问题，通过将小数据包合并后再发送来减少网络中的小包数量，优化带宽利用率
            // 然而，在某些情况下，这种合并会带来延迟，尤其是在需要低延迟的应用场景中，例如：实时网络游戏，高频交易系统
            socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 8888));
            // 设置非阻塞式
            socketChannel.configureBlocking(false);

            log.info("Client 成功连接服务端");

            while (!socketChannel.finishConnect()) {
                // 不断的自旋、等待，或者做一些其他的事情
            }

            // 发送文件名称
            ByteBuffer fileNameByteBuffer = StandardCharsets.UTF_8.encode("nio_" + file.getName());
            // 发送缓冲大小
            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            ByteBuffer buffer = ByteBuffer.allocateDirect(NioDemoConfig.SEND_BUFFER_SIZE);
            // 发送文件名称长度
//            int fileNameLen =     fileNameByteBuffer.capacity();
            // 返回 fileNameByteBuffer 的 position 位置来确定文件名有多长
            int fileNameLen = fileNameByteBuffer.remaining();
            buffer.clear();
            buffer.putInt(fileNameLen);
            // 切换到读模式
            buffer.flip();
            socketChannel.write(buffer);
            log.info("Client 文件名称长度发送完成: {}", fileNameLen);


            // 发送文件名称
            socketChannel.write(fileNameByteBuffer);
            log.info("Client 文件名称发送完成: {}", file.getName());
            // 发送文件长度
            // 清空
            buffer.clear();
            buffer.putInt((int) file.length());
            // 切换到读模式
            buffer.flip();
            // 写入文件长度
            socketChannel.write(buffer);
            log.info("Client 文件长度发送完成: {}", file.length());

            // 发送文件内容
            log.debug("开始传输文件");
            int length = 0;
            long offset = 0;
            // 清空内容，并切换到写模式
            buffer.clear();
            // 从 channel 中读取数据 写入到 buffer 中
            while ((length = fileChannel.read(buffer)) > 0) {
                // 反转
                buffer.flip();
                // 从 buffer 中读取数据，通过 socketchannel 发送到客户端
                socketChannel.write(buffer);
                // 清空内容，切换到写模式
                buffer.clear();

                // 计算一下进度
                offset += length;
                log.info("| {}% |", 100 * offset / file.length());
            }

            // 等待一分钟关闭连接
            LockSupport.parkNanos(60 * 1000L * 1000L * 1000L);
            log.info("======== 文件传输成功 ========");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
            fileChannel.close();
        }
    }

    @SneakyThrows
    public static void nioReceiveServer() {
        // 创建一个服务端的通信信道, 以这个信道创建一个可以通信的 socket
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverChannel.socket();
        // 设置这个 socket 为非阻塞,绑定的端口为 8888
        serverChannel.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(8888));

        // 将通道注册到选择器上, 并注册的IO事件为：“接收新连接”
        Selector selector = Selector.open();
        // 每个channel最多只能向 selector注册一次,注册之后就形成了固定的 selectionKey
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("serverChannel is listening...");


        // 阻塞当前线程,直到至少有一个通道准备好进行I/O操作,选择器内有信道准备好之后会返回
        while (selector.select() > 0) {

            // 返回 Selector中的所有已准备好进行I/O操作的通道的 SelectionKey集合。
            if (null == selector.selectedKeys()) {
                continue;
            }
            // 7、遍历所有已经准备好的 SelectionKey集合
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {

                // 8、获取单个的选择键，并处理
                SelectionKey key = it.next();
                if (null == key) {
                    continue;
                }

                // 9、如果是一个建立新连接的操作
                if (key.isAcceptable()) {

                    // 10、若接受的事件是“新连接”事件, 再通过 SelectionKey 获取这个 key对应的信道
                    ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = ssChannel.accept();
                    if (socketChannel == null) {
                        continue;
                    }

                    // 11、客户端新连接，切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);

                    // 这个新连接将作为一个新的信道注册到 selector 上, 并且注册一个读事件
                    SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    // 余下为业务处理
                    Session session = new Session();
                    session.remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
                    clientMap.put(socketChannel, session);
                    log.info("{} 连接成功...", socketChannel.getRemoteAddress());

                } else if (key.isReadable()) {
                    // 如果这是一个可读的事件, 那么就扔去处理数据的方法
                    handleData(key);
                }
                // NIO的特点只会累加，已选择的键的集合不会删除
                // 如果不删除，下一次又会被select函数选中
                it.remove();
            }
        }
    }

    /**
     * 服务器端保存的客户端对象，对应一个客户端文件
     */
    static class Session {
        int step = 1; // 1 读取文件名称的长度，2 读取文件名称  ，3 ，读取文件内容的长度， 4 读取文件内容
        // 文件名称
        String fileName = null;

        // 长度
        long fileLength;
        int fileNameLength;

        // 开始传输的时间
        long startTime;

        // 客户端的地址
        InetSocketAddress remoteAddress;

        // 输出的文件通道
        FileChannel fileChannel;

        // 接收长度
        long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }

    // 使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    static Map<SelectableChannel, Session> clientMap = new HashMap<SelectableChannel, Session>();

    private static final ByteBuffer buffer = ByteBuffer.allocate(1024);

    /**
     * 处理客户端传输过来的数据
     */
    private static void handleData(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        Session session = clientMap.get(key.channel());

        buffer.clear();
        while ((num = socketChannel.read(buffer)) > 0) {

            log.info("收到的字节数 = {}", num);

            // 切换到读模式
            buffer.flip();

            process(session, buffer);
            buffer.clear();
//                key.cancel();

        }
    }

    private static void process(Session session, ByteBuffer buffer) {
        while (len(buffer) > 0) {   // 客户端发送过来的，首先处理文件名长度
            if (1 == session.step) {
                int fileNameLengthByteLen = len(buffer);
                System.out.println("读取文件名称长度之前，可读取的字节数 = " + fileNameLengthByteLen);
                System.out.println("读取文件名称长度之前，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件名称长度之前，buffer.capacity() = " + buffer.capacity());
                System.out.println("读取文件名称长度之前，buffer.limit() = " + buffer.limit());
                System.out.println("读取文件名称长度之前，buffer.position() = " + buffer.position());

                if (len(buffer) < 4) {
                    log.info("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }

                // 获取文件名称长度
                session.fileNameLength = buffer.getInt();

                System.out.println("读取文件名称长度之后，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件名称长度 = " + session.fileNameLength);

                session.step = 2;

            } else if (2 == session.step) {
                log.info("step 2");

                if (len(buffer) < session.fileNameLength) {
                    log.info("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }

                byte[] fileNameBytes = new byte[session.fileNameLength];


                // 读取文件名称
                buffer.get(fileNameBytes);


                // 文件名
                String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
                // System.out.println("读取文件名称 = " + fileName);
                session.fileName = fileName;

                // 确定上传目录,以及保存内容的文件 NIOTest.class.getResource("/")可以得到当前类的根目录的绝对路径
                String dirPath = JvmUtil.isWin() ? NIOTest.class.getResource("/").getPath().substring(1) + "upload" : NIOTest.class.getResource("/").getPath() + "upload";
                File directory = new File(dirPath);
                if (!directory.exists()) {
                    directory.mkdir();
                }

                // 确定上传的文件的文件名
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);

                try {
                    if (!file.exists()) {
                        file.createNewFile();

                    }
                    FileChannel fileChannel = new FileOutputStream(file).getChannel();
                    session.fileChannel = fileChannel;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                session.step = 3;

            } else if (3 == session.step) {
                log.info("step 3");

                // 客户端发送过来的，首先处理文件内容长度

                if (len(buffer) < 4) {
                    log.info("出现半包问题，需要更加复制的拆包方案");
                    throw new RuntimeException("出现半包问题，需要更加复制的拆包方案");
                }
                // 获取文件内容长度
                session.fileLength = buffer.getInt();

                System.out.println("读取文件内容长度之后，buffer.remaining() = " + buffer.remaining());
                System.out.println("读取文件内容长度 = " + session.fileLength);

                session.step = 4;
                session.startTime = System.currentTimeMillis();

            } else if (4 == session.step) {

                log.info("step 4");
                // 客户端发送过来的，最后是文件内容

                session.receiveLength += len(buffer);

                // 写入文件
                try {
                    session.fileChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (session.isFinished()) {
                    finished(session);
                }
            }
        }
    }

    private static void finished(Session session) {
        log.info("上传完毕");
        log.info("文件接收成功,File Name：{}", session.fileName);
        log.info(" Size：{}", getFormatFileSize(session.fileLength));
        long endTime = System.currentTimeMillis();
        log.info("NIO IO 传输毫秒数：{}", endTime - session.startTime);
    }

    private static int len(ByteBuffer buffer) {

        log.info(" >>>  buffer left：{}", buffer.remaining());
        return buffer.remaining();
    }

    private static DecimalFormat fileSizeFormater = decimalFormat(1);

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
        URL url = NIOTest.class.getResource(resName);
        String path;
        if (null == url) {
            path = NIOTest.class.getResource("/").getPath() + resName;
        } else {
            path = url.getPath();
        }
        String decodePath = URLDecoder.decode(path, "UTF-8");
        // 如果是 Windows 系统, 路径前缀会有一个 /,这里删除这个无效的 /
        if (JvmUtil.isWin()) {
            return decodePath.substring(1);
        }
        return decodePath;
    }
}
