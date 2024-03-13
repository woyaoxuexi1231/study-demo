package com.hundsun.demo.springboot.dynamicdb.config;

import com.hundsun.demo.springboot.dynamicdb.core.DynamicDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: DynamicDataSourceConfi
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 19:19
 */
@Configuration
@Conditional(DynamicDataSourceEnableConfig.class)
@Import(DynamicDataSourceAspect.class)
public class DynamicDataSourceConfig {

    @Autowired
    HikariDataSourceFirstConfig hikariDataSourceFirstConfig;

    @Autowired
    HikariDataSourceSecondConfig hikariDataSourceSecondConfig;

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

    @Bean
    @Primary
    public DataSource dataSource() {
        return new DynamicDataSource();
    }
}
