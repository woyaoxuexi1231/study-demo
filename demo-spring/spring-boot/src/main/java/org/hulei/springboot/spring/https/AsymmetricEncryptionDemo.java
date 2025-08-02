package org.hulei.springboot.spring.https;

import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class AsymmetricEncryptionDemo {

    public static void main(String[] args) throws Exception {
        /*
        非对称加密

          - 非对称加密：使用一对密钥（公钥和私钥）
          - 公钥加密，私钥解密
          - 常见算法：RSA、ECC
          - 优点：不需要共享私钥，适合做密钥交换、数字签名
          - 缺点：计算较慢，不适合大量数据加密
         */

        String data = "Hello, RSA!";

        // ====================================== 生成 AES 密钥 ===============================
        // KeyPairGenerator：用于生成非对称密钥对的工具类。 RSA 使用 公钥加密、私钥解密
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 指定密钥长度为 2048 位（安全推荐值，低于 1024 位已不安全）。
        keyPairGen.initialize(2048);
        // 包含公钥和私钥的对象。
        KeyPair keyPair = keyPairGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        // ====================================== 加密数据 ====================================
        Cipher encryptCipher = Cipher.getInstance("RSA");
        // 初始化加密模式，传入公钥。
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = encryptCipher.doFinal(data.getBytes());
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));


        // ====================================== 解密数据 ====================================
        Cipher decryptCipher = Cipher.getInstance("RSA");
        // 初始化解密模式，传入私钥。
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = decryptCipher.doFinal(encrypted);
        System.out.println("Decrypted: " + new String(decrypted));
    }
}
