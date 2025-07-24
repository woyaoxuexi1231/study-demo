package com.hundsun.demo.springcloud.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2023/5/5 20:44
 */

@MapperScan(basePackages = "com.hundsun.demo.springcloud.security.mapper")
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        /*
        在没有任何额外的配置和依赖的情况下，Spring Security 有一套默认的运行状态。
        默认用户为 user，密码在控制台以日志形式输出（Using generated security password）。

        在 Cookie 中可以找到一个 JSESSIONID 的变量，这个变量就是保存登录状态的关键。
        如果删掉，那么登录状态将会立马失效。因为前台无法再传递有效的会话ID。
         */
        SpringApplication.run(SecurityApplication.class, args);
    }
}
