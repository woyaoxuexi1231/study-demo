package org.hulei.springdata.jdbc;

import org.hulei.springdata.routingdatasource.config.coding.EnableCodingRoutingDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hulei
 * @since 2024/10/10 16:00
 */

@EnableCodingRoutingDataSource
@MapperScan(basePackages = "org.hulei.springdata.jdbc.mapper")
@SpringBootApplication
public class SpringDataJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJdbcApplication.class, args);
    }
}
