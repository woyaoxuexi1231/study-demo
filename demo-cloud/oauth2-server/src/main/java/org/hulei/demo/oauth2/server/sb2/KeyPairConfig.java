// package org.hulei.demo.oauth2.server.sb2;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// import java.security.KeyPair;
// import java.security.KeyPairGenerator;
// import java.security.NoSuchAlgorithmException;
//
// // 添加一个配置类来生成 KeyPair
// @Configuration
// class KeyPairConfig {
//
//     @Bean
//     public KeyPair keyPair() throws NoSuchAlgorithmException {
//         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//         keyPairGenerator.initialize(2048); // 密钥大小
//         return keyPairGenerator.generateKeyPair();
//     }
// }