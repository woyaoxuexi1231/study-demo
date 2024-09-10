package org.hulei.keeping.server.db.dynamicdb.config.coding;

import org.hulei.keeping.server.db.dynamicdb.annotation.DataSourceToggleAop;
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
 * 数据源的配置类
 * 1. 单纯配置了两个数据源
 * 2. 以我们新建的动态数据源 DynamicDataSource 输出 dataSource
 *
 * @author h1123
 * @since 2023/2/25 19:19
 */

@Configuration
@Conditional(EnableCodingRoutingDBConfig.class)
@Import(DataSourceToggleAop.class)
public class RoutingDataSourceBeanConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        return new RoutingCodingDataSource();
    }

    @Autowired
    FirstHikariDataSourceConfig firstHikariDataSourceConfig;

    @Autowired
    SecondHikariDataSourceConfig secondHikariDataSourceConfig;

    @Bean(value = "dataSourceFirst")
    public DataSource dataSourceFirst() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(firstHikariDataSourceConfig.getUrl());
        config.setUsername(firstHikariDataSourceConfig.getUsername());
        config.setPassword(firstHikariDataSourceConfig.getPassword());
        config.setPoolName(String.format("coding-first-%s", firstHikariDataSourceConfig.getUrl().substring("jdbc:mysql://".length(), firstHikariDataSourceConfig.getUrl().lastIndexOf("?"))));
        return new HikariDataSource(config);
    }

    @Bean
    public DataSource dataSourceSecond() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(secondHikariDataSourceConfig.getUrl());
        config.setUsername(secondHikariDataSourceConfig.getUsername());
        config.setPassword(secondHikariDataSourceConfig.getPassword());
        config.setPoolName(String.format("coding-second-%s", secondHikariDataSourceConfig.getUrl().substring("jdbc:mysql://".length(), secondHikariDataSourceConfig.getUrl().lastIndexOf("?"))));
        return new HikariDataSource(config);
    }
}
