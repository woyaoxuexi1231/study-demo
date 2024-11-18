package org.hulei.springboot.tkmybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author hulei
 * @since 2024/9/17 16:38
 */

@MapperScan(basePackages = {"org.hulei.springboot.tkmybatis.mapper"})
@SpringBootApplication
public class TkmybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TkmybatisApplication.class, args);
    }
}
