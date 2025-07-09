package org.hulei.basic.jdk.io.client;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author hulei
 * @since 2025/7/8 12:49
 */

@SuppressWarnings("CallToPrintStackTrace")
public class SimpleClient {

    public static void main(String[] args) {
        try (Socket s = new Socket("127.0.0.1", 8105);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        ) {

            while (true) {
                // 从控制台读取用户输入
                System.out.print("输入发送到服务器的消息: ");
                String userMessage = userInput.readLine();
                // 向服务器端发送消息
                bw.write(userMessage + "\n");
                bw.flush();
                // 可以加入退出条件，比如用户输入 "exit" 时退出循环
                if ("exit".equalsIgnoreCase(userMessage)) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
