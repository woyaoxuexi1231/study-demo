package org.hulei.basic.jdk.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.network
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-05-13 17:44
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@SuppressWarnings("CallToPrintStackTrace")
@Slf4j
public class Client {

    public static void main(String[] args) {

        try {
            Socket s = new Socket("127.0.0.1", 8100);

            // 构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            // 发送缓冲
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            // 读取缓冲
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // 增加一个读取用户输入的部分
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                // 从控制台读取用户输入
                System.out.print("输入发送到服务器的消息: ");
                String userMessage = userInput.readLine();

                // 向服务器端发送消息
                bw.write(userMessage + "\n");
                bw.flush();

                // 读取一行，遇到换行或者EOF会停止读取。 **注意：这个命令是阻塞的
                String mess = br.readLine();
                System.out.println("服务器：" + mess);

                // 可以加入退出条件，比如用户输入 "exit" 时退出循环
                if ("exit".equalsIgnoreCase(userMessage)) {
                    break;
                }
            }

            // 关闭资源
            br.close();
            bw.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}