package org.hulei.springboot.utils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hulei
 * @since 2024/11/20 17:21
 */

@Slf4j
@RequiredArgsConstructor
public class JSchExecutorController {

    private final JschConfig jschConfig;

    private Session session;

    private final String upstreamTemplate = "\n" +
            "\n" +
            "    upstream " + "%s" + "_" + "%s" + " {\n" +
            "        server " + "%s" + ":" + "%s" + ";\n" +
            "    }\n" +
            "\n" +
            "    server {\n" +
            "        listen " + "%s" + " so_keepalive=on;\n" +
            "        proxy_pass " + "%s" + "_" + "%s" + ";\n" +
            "        proxy_connect_timeout 2s;\n" +
            "        proxy_timeout 900s;\n" +
            "        error_log /var/log/nginx/" + "%s" + "_" + "%s" + ".error.log;\n" +
            "    }\n";

    @PostConstruct
    private void initJSchExecutor() {
        try {
            connect();
        } catch (Exception e) {
            log.error("连接nginx服务器失败，后续更新路由会出现问题！");
        }
    }

    // 建立连接的方法，带自动重连机制
    private void ensureConnected() throws JSchException {
        if (session == null || !session.isConnected()) {
            log.info("Session is not connected. Reconnecting...");
            connect();
        }
    }

    // 连接到远程服务器
    private void connect() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(jschConfig.sshUsername, jschConfig.sshHost, jschConfig.sshPort);
        session.setPassword(jschConfig.sshPassword);

        // 设置配置以避免HostKey验证
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        log.info("Connecting to {}:{} ...", jschConfig.sshHost, jschConfig.sshPort);
        session.connect();
        log.info("Connected!");
    }

    // 断开连接
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("Disconnected!");
        }
    }

    // 创建文件
    public void createUpstreamConfig(){
        String.format(upstreamTemplate,"upstream","1");
    }


    // 执行命令并打印结果（包含自动重连机制）
    public void executeCommand(String command) {
        try {
            ensureConnected(); // 确保连接正常
            log.info("Executing command: " + command);

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
                        log.info("Exit status: {}", channel.getExitStatus());
                        break;
                    }
                }
            } finally {
                if (channel != null) {
                    channel.disconnect();
                }
            }
        } catch (JSchException e) {
            log.error("SSH connection issue: {}", e.getMessage());
        } catch (Exception e) {
            log.error("执行命令过程中出现问题", e);
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

            log.info("Executing command: " + command);

            this.executeCommand(command);

        } catch (JSchException e) {
            log.error("SSH connection issue: {}", e.getMessage());
        } catch (Exception e) {
            log.error("执行命令过程中出现问题", e);
        }
    }

    public static String normalize(String path) {
        // 使用 Paths.get 处理路径
        Path p = Paths.get(path);

        // 使用 Path 的 normalize 方法来规范化路径
        Path normalizedPath = p.normalize();

        // 将 Path 转换为字符串
        String normalizedString = normalizedPath.toString();

        // 将系统默认分隔符替换为正斜杠
        normalizedString = normalizedString.replace("\\", "/");

        // 去除多余的斜杠（如果有）
        normalizedString = normalizedString.replaceAll("/{2,}", "/");

        return normalizedString;
    }
}
