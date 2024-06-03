package com.hundsun.demo.spring.jdk.encode;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESExample {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "YourSecretKey222"; // 需要是16、24或32个字节长

    public static String encrypt(String value) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public static String decrypt(String encryptedValue) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decryptedValue = cipher.doFinal(decodedValue);
        return new String(decryptedValue);
    }

    public static void main(String[] args) throws Exception {
        String originalValue = "Hello, world!";
        System.out.println("原始值: " + originalValue);

        String encryptedValue = encrypt(originalValue);
        System.out.println("加密后的值: " + encryptedValue);

        String decryptedValue = decrypt(encryptedValue);
        System.out.println("解密后的值: " + decryptedValue);
    }
}