package org.hulei.springdata.routingdatasource.config.coding;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author hulei
 * @since 2024/10/10 20:44
 */

@ConditionalOnProperty(value = {"spring.datasource.routing.coding.enable"}, havingValue = "true")
@Configuration
@Import(value = {HikariDataSourceFirstConfig.class, HikariDataSourceSecondConfig.class})
public class RoutingCodingDataSourceAutoConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSource dataSourceFirst, DataSource dataSourceSecond) {
        return new RoutingCodingDataSource(dataSourceFirst, dataSourceSecond);
    }

    @Autowired
    HikariDataSourceFirstConfig hikariDataSourceFirstConfig;

    @Autowired
    HikariDataSourceSecondConfig hikariDataSourceSecondConfig;

    @Bean(value = "dataSourceFirst")
    public DataSource dataSourceFirst() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(hikariDataSourceFirstConfig.getUrl());
        config.setUsername(hikariDataSourceFirstConfig.getUsername());
        config.setPassword(hikariDataSourceFirstConfig.getPassword());
        config.setPoolName(String.format("coding-first-%s", hikariDataSourceFirstConfig.getUrl().substring("jdbc:mysql://".length(), hikariDataSourceFirstConfig.getUrl().lastIndexOf("?"))));
        return new HikariDataSource(config);
    }

    @Bean(value = "dataSourceSecond")
    public DataSource dataSourceSecond() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(hikariDataSourceSecondConfig.getUrl());
        config.setUsername(hikariDataSourceSecondConfig.getUsername());
        config.setPassword(hikariDataSourceSecondConfig.getPassword());
        config.setPoolName(String.format("coding-second-%s", hikariDataSourceSecondConfig.getUrl().substring("jdbc:mysql://".length(), hikariDataSourceSecondConfig.getUrl().lastIndexOf("?"))));
        return new HikariDataSource(config);
    }
}
