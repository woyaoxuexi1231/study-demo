package com.hundsun.demo.springboot.db.dynamicdb.config;

import com.hundsun.demo.springboot.db.dynamicdb.core.DynamicDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 数据源的配置类
 * 1. 单纯配置了两个数据源
 * 2. 以我们新建的动态数据源 DynamicDataSource 输出 dataSource
 *
 * @author h1123
 * @since 2023/2/25 19:19
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

@ConfigurationProperties(prefix = "spring.datasource.first")
@Component
@Data
class HikariDataSourceFirstConfig {

    private String url;

    private String username;

    private String password;

}

@ConfigurationProperties(prefix = "spring.datasource.second")
@Component
@Data
class HikariDataSourceSecondConfig {

    private String url;

    private String username;

    private String password;

}

/**
 * 这是一个动态数据源是否开启的配置类,读取spring.datasource.dynamic.enable这个来决定是否开启多数据源
 *
 * @author h1123
 * @since 2023/2/25 19:13
 */

class DynamicDataSourceEnableConfig implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return conditionContext.getEnvironment().getProperty("spring.datasource.dynamic.enable").equals("true");
    }
}
