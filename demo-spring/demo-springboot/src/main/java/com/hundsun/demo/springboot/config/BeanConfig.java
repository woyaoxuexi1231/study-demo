package com.hundsun.demo.springboot.config;

import com.hundsun.demo.commom.core.aop.DoneTimeAspect;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/12 2:12
 */
@Configuration
public class BeanConfig {

    @Autowired
    HikariDataSourceFirstConfig hikariDataSourceFirstConfig;

    @Autowired
    HikariDataSourceSecondConfig hikariDataSourceSecondConfig;

    @Bean
    public DoneTimeAspect doneTimeAspect() {
        return new DoneTimeAspect();
    }

    @Bean
    public DataSource dataSourceFirst() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(hikariDataSourceFirstConfig.getUrl());
        config.setUsername(hikariDataSourceFirstConfig.getUsername());
        config.setPassword(hikariDataSourceFirstConfig.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public DataSource dataSourceSecond() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(hikariDataSourceSecondConfig.getUrl());
        config.setUsername(hikariDataSourceSecondConfig.getUsername());
        config.setPassword(hikariDataSourceSecondConfig.getPassword());
        return new HikariDataSource(config);
    }
}
