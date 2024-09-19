package org.hulei.springboot.jdbc;

import org.hulei.springboot.jdbc.dynamicdb.config.parsing.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/9/19 21:09
 */

@EnableDynamicDataSource
@MapperScan(basePackages = {"org.hulei.springboot.mybatisplus.mapper"})
@SpringBootApplication
public class SpringJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJdbcApplication.class,args);
    }
}
