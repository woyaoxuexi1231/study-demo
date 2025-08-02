package org.hulei.springboot.spring.https;

import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

public class HttpsServerExample {
    public static void main(String[] args) throws Exception {

        // 1. 加载 keystore（包含私钥 + 证书）
        char[] password = "123456".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("myclientkeystore.jks"), password);

        // 2. 初始化 KeyManager
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        // 3. 初始化 SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        // 4. 启动 HTTPS 服务器
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8443), 0);
        server.setHttpsConfigurator(new com.sun.net.httpserver.HttpsConfigurator(sslContext));
        server.createContext("/hello", new HelloHandler());
        server.setExecutor(null); // 默认线程池
        server.start();

        System.out.println("HTTPS Server started on https://localhost:8443/hello");
    }

    static class HelloHandler implements HttpHandler {
        public void handle(HttpExchange exchange) {
            try {
                String response = "Hello from secure server!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
