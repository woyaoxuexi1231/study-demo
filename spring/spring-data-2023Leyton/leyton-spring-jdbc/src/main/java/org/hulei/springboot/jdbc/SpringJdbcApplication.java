package org.hulei.springboot.jdbc;

import org.apache.ibatis.annotations.Mapper;
import org.hulei.springdata.routingdatasource.config.parsing.EnableDynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/9/19 21:09
 */

@EnableDynamicDataSource
@MapperScan(basePackages = "org.hulei.springboot.jdbc.transactional.mapper")
@RestController
@SpringBootApplication
public class SpringJdbcApplication implements ApplicationContextAware {

    ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SpringJdbcApplication.class, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/debug")
    public void debug() {
        System.out.println("debug");
    }
}
