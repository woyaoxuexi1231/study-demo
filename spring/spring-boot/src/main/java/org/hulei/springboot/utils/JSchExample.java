package org.hulei.springboot.utils;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSchExample {

    public static void main(String[] args) {
        String username = "root"; // 替换为实际的SSH用户名
        String host = "192.168.3.233";         // 替换为实际的SSH主机地址
        int port = 22;                     // SSH默认端口是22
        String password = "2Kryspr!"; // 替换为实际的密码

        JSch jsch = new JSch();
        Session session = null;

        try {
            // 1. 创建并配置会话
            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // 设置配置以避免HostKey验证
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 2. 连接到服务器
            System.out.println("Connecting to " + host + "...");
            session.connect();
            System.out.println("Connected!");

            // 3. 打开通道并执行命令
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            // String command = "ls -l"; // 要执行的命令

            String command = "/usr/local/nginx/sbin/nginx -s reload";

            channel.setCommand(command);

            // 获取命令输出流
            InputStream inputStream = channel.getInputStream();
            InputStream err = channel.getErrStream();
            channel.connect();
            System.out.println("Command executed: " + command);

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
                    if (channel.getExitStatus() != 0) {
                        // 读取命令的错误输出
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(err))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.err.println(line);
                                String regex = "nginx\\.pid.*No such file or directory";
                                if(line.matches(".*" + regex + ".*")){
                                    return;
                                }
                            }
                        }
                    }
                    break;
                }
                Thread.sleep(1000);
            }

            // 4. 断开连接
            channel.disconnect();
            session.disconnect();
            System.out.println("Disconnected!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
