package org.hulei.springdata.jdbc;

import org.hulei.springdata.routingdatasource.config.parsing.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/10/10 16:00
 */

// @EnableDynamicDataSource
@MapperScan(basePackages = "org.hulei.springdata.jdbc.transactional.mapper")
@SpringBootApplication
public class SpringDataJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJdbcApplication.class, args);
    }
}
