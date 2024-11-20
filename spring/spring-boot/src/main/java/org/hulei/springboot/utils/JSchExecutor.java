package org.hulei.springboot.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.InputStream;

public class JSchExecutor {

    private String username;
    private String host;
    private int port;
    private String password;
    private Session session;

    public JSchExecutor(String username, String host, int port, String password) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.password = password;
    }

    // 建立连接的方法，带自动重连机制
    private void ensureConnected() throws JSchException {
        if (session == null || !session.isConnected()) {
            System.out.println("Session is not connected. Reconnecting...");
            connect();
        }
    }

    // 连接到远程服务器
    public void connect() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, port);
        session.setPassword(password);

        // 设置配置以避免HostKey验证
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        System.out.println("Connecting to " + host + "...");
        session.connect();
        System.out.println("Connected!");
    }

    // 执行命令并打印结果（包含自动重连机制）
    public void executeCommand(String command) {
        try {
            ensureConnected(); // 确保连接正常
            System.out.println("Executing command: " + command);

            ChannelExec channel = null;
            try {
                channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);

                InputStream inputStream = channel.getInputStream();
                channel.connect();

                // 读取并打印命令输出
                byte[] tmp = new byte[1024];
                while (true) {
                    while (inputStream.available() > 0) {
                        int i = inputStream.read(tmp, 0, 1024);
                        if (i < 0) break;
                        System.out.print(new String(tmp, 0, i));
                    }
                    if (channel.isClosed()) {
                        System.out.println("\nExit status: " + channel.getExitStatus());
                        break;
                    }
                    Thread.sleep(1000);
                }
            } finally {
                if (channel != null) {
                    channel.disconnect();
                }
            }
        } catch (JSchException e) {
            System.err.println("SSH connection issue: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 断开连接
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            System.out.println("Disconnected!");
        }
    }


    // 在服务器的指定位置创建文件并写入内容
    public void createFileWithContent(String remoteFilePath, String content) {
        try {
            ensureConnected(); // 确保连接正常

            // 使用 echo 命令和重定向符号 > 来创建文件并写入内容
            // 注意：如果内容中包含特殊字符（如空格、引号等），可能需要对内容进行适当的转义
            // 这里我们假设内容不包含需要转义的特殊字符，或者你可以自行添加转义逻辑
            String command = "echo '" + content.replace("'", "\\'") + "' > " + remoteFilePath;

            System.out.println("Executing command: " + command);

            this.executeCommand(command);

        } catch (JSchException e) {
            System.err.println("SSH connection issue: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 测试方法
    public static void main(String[] args) {
        String username = "root";
        String host = "192.168.3.233";
        int port = 22;
        String password = "2Kryspr!";

        String upstreamHost = "192.168.3.233";
        int upstreamPort = 9999;
        int listenPort = 9998;

        String upstreamName = "spring_boot";
        String id = "1";

        String upstreamConfigPath = "/usr/local/nginx/conf/upstream/";

        String nginxSbinPath = "/usr/local/nginx/sbin";
        String nginxConfigPath = "/usr/local/nginx/conf";

        JSchExecutor executor = new JSchExecutor(username, host, port, password);

        try {
            executor.connect();
            executor.executeCommand("ls -l");
            executor.executeCommand("pwd");
            executor.executeCommand("echo 'Hello from JSch!'");


            executor.createFileWithContent(upstreamConfigPath + upstreamName + "_" + id + ".conf", "\n" +
                    "\n" +
                    "    upstream " + upstreamName + "_" + id + " {\n" +
                    "        server " + upstreamHost + ":" + upstreamPort + ";\n" +
                    "    }\n" +
                    "\n" +
                    "    server {\n" +
                    "        listen " + listenPort + " so_keepalive=on;\n" +
                    "        proxy_pass " + upstreamName + "_" + id + ";\n" +
                    "        proxy_connect_timeout 2s;\n" +
                    "        proxy_timeout 900s;\n" +
                    "        error_log /var/log/nginx/" + upstreamName + "_" + id + ".error.log;\n" +
                    "    }\n");

            // 重启 nginx
            executor.executeCommand(nginxSbinPath + "/nginx" + " -s stop");

            executor.executeCommand(nginxSbinPath + "/nginx -c " + nginxConfigPath + "/nginx.conf");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.disconnect();
        }
    }
}
