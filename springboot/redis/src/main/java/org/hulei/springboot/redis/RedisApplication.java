package org.hulei.springboot.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/9/19 21:57
 */

@MapperScan(basePackages = {"org.hulei.springboot.mybatisplus.mapper"})
@SpringBootApplication
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
