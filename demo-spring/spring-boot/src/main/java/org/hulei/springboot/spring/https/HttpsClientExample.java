package org.hulei.springboot.spring.https;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class HttpsClientExample {
    public static void main(String[] args) throws Exception {
        // 1. 创建一个 TrustManager，信任所有证书（仅开发用！）
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        // 2. 初始化 SSLContext
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());

        // 3. 设置默认的 Socket 工厂
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // 4. 关闭主机名验证（因为是 localhost）
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // 5. 发起请求
        URL url = new URL("https://localhost:8443/hello");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}
