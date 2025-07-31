package org.hulei.basic.jdk.io;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author hulei
 * @since 2024/9/14 12:05
 */

@SuppressWarnings("resource")
public class SingleConnectionServer {

    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket serverSocket = new ServerSocket(8104);
        Socket accept = serverSocket.accept();
        System.out.println("Accepted connection from " + accept);

        InputStream inputStream = accept.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String msg;
        while ((msg = reader.readLine()) != null) {
            System.out.println("received message: " + msg);
        }
    }
}
