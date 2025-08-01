package com.hundsun.demo.springcloud.security.passwordencoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/**
 * @author hulei
 * @since 2025/8/1 18:57
 */

@Configuration
public class EncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
        spring security 内置使用 PasswordEncoder 接口来实现的密码加密
          - 当用数据库存储用户密码时，加密过程用encode方法
          - matches方法用于判断用户登录时输入的密码是否正确。

        spring 内置的加密算法有如下：
          🌟BCryptPasswordEncoder - BCrypt 是基于 Blowfish 密码的哈希算法
            - 使用随机盐值（salt）防止彩虹表攻击
            - 内置工作因子（迭代次数）可调整计算成本
            - 生成的哈希值格式：$2a$[cost]$[22 character salt][31 character hash]
          🌟Pbkdf2PasswordEncoder - PBKDF2 (Password-Based Key Derivation Function 2)
            - 使用 HMAC 作为伪随机函数
            - 需要指定迭代次数、密钥长度和盐值
            - 抗暴力破解，但不如 BCrypt 和 Argon2 安全
          🌟SCryptPasswordEncoder - SCrypt 是一种内存密集型算法
            - 设计目的是抵抗大规模定制硬件攻击
            - 需要大量内存计算，增加硬件攻击成本
            - 参数：CPU成本、内存成本、并行化参数、密钥长度、盐长度
          🌟Argon2PasswordEncoder
            - 提供三种变体：Argon2d、Argon2i、Argon2id
            - 抵抗侧信道攻击和GPU/ASIC攻击
            - 参数：迭代次数、内存大小、并行度、哈希长度、盐长度
          🌟MessageDigestPasswordEncoder(已废弃) - 基于标准Java MessageDigest的简单哈希
            - 支持MD5、SHA-1、SHA-256等
            - 安全性较低，不推荐用于生产环境
            - 容易被暴力破解
          🌟NoOpPasswordEncoder(仅用于测试) - 明文
            - 不进行任何加密，明文存储密码
            - 安全性为零

        | 算法       | 安全性 | 性能 | 特点               | 适用场景             |
        | `BCrypt`  | 高    | 中   |  自动加盐、慢速      | 绝大多数Web系统，通用推荐 ✅ |
        | `PBKDF2`  | 高    | 中高 | 可配置、兼容性强     | 老系统、兼容Java EE规范  |
        | `scrypt`  | 更高  | 较低 | 高内存成本，抗GPU攻击 | 区块链、钱包系统 🔐      |
        | `Argon2`  | 最高  | 中  | 最新、抗侧信道攻击     | 高安全系统，现代推荐 🔒    |
         */
        PasswordEncoder onOpPasswordEncoder = NoOpPasswordEncoder.getInstance();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(
                12 // 控制哈希计算的工作因子（迭代次数），值越大安全性越高但计算时间越长。强度每增加1，计算时间大约翻倍
        );
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder(
                "secret", // 额外密钥(可选)，可为空或固定字符串
                32, // 盐值长度(字节)
                600000,  // 迭代次数
                256 // 输出哈希值的位数(bit)
        );
        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder(
                16384,  // CPU cost (N)     主要工作因子，必须是2的幂
                8,      // Memory cost (r)          内存使用乘数（块大小）
                1,      // Parallelization (p)  	并行化参数
                32,     // Key length  	            输出密钥长度（字节）
                64      // Salt length              盐值长度（字节）
        );
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(
                16,     // 盐长度
                32,     // 哈希长度
                1,      // 并行度
                65536,  // 内存大小(KB)
                10      // 迭代次数
        );

        return bCryptPasswordEncoder;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        System.out.println(bCryptPasswordEncoder.encode("1"));
        // $2a$12$RwDIMpb3AWtkl3tA0Q.PAOYaHEdfbj8DIEOsq/mzjHh8mbA/o7Wae
    }
}
