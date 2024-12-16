package org.hulei.jdk.encode;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class RSAExample {
    public static void main(String[] args) throws Exception {
        // Generate Key Pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Data to be encrypted
        String data = "Hello, world!";

        // Encrypt data using public key
        byte[] encryptedData;
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedData = cipher.doFinal(data.getBytes());

        // Decrypt data using private key
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        String decryptedText = new String(decryptedData);
        System.out.println("Decrypted Data: " + decryptedText);
    }
}