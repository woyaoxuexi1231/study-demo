package org.hulei.springboot.spring.https;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.Security;


public class SM4Example {

    static {
        Security.addProvider(new BouncyCastleProvider()); // 全局注册
    }

    public static void main(String[] args) throws Exception {

        /*
        国密加密
        SM4是一种分组加密算法，采用128位分组长度和128位密钥长度，属于对称加密（加密和解密使用相同密钥）。
         */

        // 1. 生成 SM4 密钥
        KeyGenerator keyGen = KeyGenerator.getInstance("SM4", "BC");
        keyGen.init(128); // SM4 密钥长度固定为128位
        SecretKey key = keyGen.generateKey();

        // 2. 初始化 Cipher（CBC 模式需 IV）
        byte[] iv = new byte[16]; // 实际项目中应使用随机IV
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        // 3. 加密数据
        String plainText = "敏感数据";
        byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));
        System.out.println("加密结果: " + bytesToHex(encryptedData));

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedData = cipher.doFinal(encryptedData);
        // 将解密后的字节数组转为字符串并打印。
        System.out.println("Decrypted: " + new String(decryptedData));
    }

    // 字节数组转十六进制字符串（辅助方法）
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}