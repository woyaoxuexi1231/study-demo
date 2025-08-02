package org.hulei.springboot.spring.https;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SymmetricEncryptionDemo {

    public static void main(String[] args) throws Exception {
        /*
        对称加密

          - 对称加密：加密和解密使用相同的密钥。
          - 常见算法：AES、DES 等。
          - 优点：速度快，适合加密大量数据。
          - 缺点：密钥传输是个问题（如何安全地传递密钥？）
         */

        String data = "Hello, World!";
        
        // ====================================== 生成 AES 密钥 ===============================
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 指定密钥长度为 128 位（可选 128/192/256，256 位需安装 JCE 无限强度策略文件）。
        SecretKey secretKey = keyGen.generateKey(); // 最终生成的密钥，类型为 SecretKey，用于加密和解密。


        // ====================================== 加密数据 ====================================
        // 获取 AES 加密算法的 Cipher 实例（默认使用 AES/ECB/PKCS5Padding）。
        // Cipher 是 Java 提供的加密解密核心类（javax.crypto.Cipher），用于执行 对称加密（如 AES）、非对称加密（如 RSA） 或 流加密。
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化 Cipher 为 加密模式，并传入密钥。
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 对数据进行加密，返回 byte[] 形式的密文。
        byte[] encrypted = cipher.doFinal(data.getBytes());
        // 将二进制密文转为 Base64 字符串，便于打印或传输。
        // Base64 是一种将 二进制数据 编码为 可打印 ASCII 字符串 的算法（如 aGVsbG8=）
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));


        // ====================================== 解密数据 ====================================
        // 重新初始化 Cipher 为 解密模式，使用相同的密钥。
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 对密文进行解密，返回原始数据的 byte[]。
        byte[] decrypted = cipher.doFinal(encrypted);
        // 将解密后的字节数组转为字符串并打印。
        System.out.println("Decrypted: " + new String(decrypted));

    }
}
