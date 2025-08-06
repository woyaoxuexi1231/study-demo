package org.hulei.springboot.spring.https;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtExample {


    /*
    一个 JWT 由三部分组成，用 . 分隔：header.payload.signature
        Header：包含令牌类型和签名算法
        Payload：存放实际数据（声明）
        Signature：对前两部分的签名，防止篡改


    JWT 的本质：JWT 并不是“替代对称加密”，它是一个标准化的令牌格式，目的是解决跨服务、无状态认证、信息封装与验证的一整套问题。
     */

    // 用于生成一个 安全的随机密钥，专门用于 HS256（HMAC-SHA256） 签名算法。
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 3600000; // 1小时

    // 生成JWT Token
    public static String generateToken(String username) {
        return Jwts.builder()
                // Payload 部分
                .setSubject(username) // 设置 JWT 的主题（sub 声明）。sub 是 JWT 标准声明之一，通常用于标识用户。
                .claim("role", "user") // 添加一个自定义声明（role）到 JWT 的 Payload 中。自定义声明可以存储任意用户相关的信息（如权限、部门等），但需避免敏感数据。
                .setIssuedAt(new Date()) // 设置 JWT 的签发时间（iat 声明）。iat 是标准声明，用于记录令牌生成时间，通常用于验证令牌是否过期。
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置 JWT 的过期时间（exp 声明）。exp 是标准声明，令牌在此时间后失效，需重新生成。

                // Header（头部）
                .signWith(SECRET_KEY) // 指定签名算法和密钥，用于生成 JWT 的签名部分。常用算法：HS256（对称加密）、RS256（非对称加密）。

                .compact();  // 生成最终的 JWT 字符串。此方法会序列化 Header 和 Payload，并计算签名，最终拼接成三段式字符串。
    }

    // 验证JWT Token
    public static boolean validateToken(String token) {

        try {
            // 1. 创建解析器，并设置签名密钥（HS256 或 RS256）
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 用于验证签名的密钥
                .build()  // 构建解析器
                .parseClaimsJws(token); // 尝试解析 JWT

            // 2. 如果解析成功，返回 true（JWT 有效）
            return true;
        } catch (Exception e) {

            // 3. 如果抛出任何异常，返回 false（JWT 无效）
            return false;
        }
    }

    // 从Token中获取用户名
    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()         // 1. 创建 JWT 解析器构建器
                .setSigningKey(SECRET_KEY)  // 2. 设置签名验证密钥
                .build()                    // 3. 构建解析器
                .parseClaimsJws(token)  // 4. 解析并验证 JWT
                .getBody()                  // 5. 获取 JWT 的载荷（Claims）
                .getSubject()               // 6. 从载荷中提取 `sub` 字段
                ;
    }

    public static void main(String[] args) {

        // 示例用法
        String token = generateToken("admin");
        System.out.println("Generated Token: " + token);
        
        boolean isValid = validateToken(token);
        System.out.println("Is token valid? " + isValid);
        
        if (isValid) {
            String username = getUsernameFromToken(token);
            System.out.println("Username from token: " + username);
        }
    }
}