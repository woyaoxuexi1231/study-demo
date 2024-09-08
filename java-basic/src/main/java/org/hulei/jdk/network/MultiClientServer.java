package org.hulei.jdk.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiClientServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8888);
            System.out.println("启动服务器....");

            while (true) {
                Socket s = ss.accept();
                System.out.println("客户端:" + s.getInetAddress().getHostAddress() + " 已连接到服务器");

                // 为每个客户端连接创建一个新的线程
                new Thread(new ClientHandler(s)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 处理每个客户端连接的线程类
class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            String mess;
            while ((mess = br.readLine()) != null) {
                System.out.println("客户端：" + mess);
                bw.write("服务器收到: " + mess + "\n");
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}