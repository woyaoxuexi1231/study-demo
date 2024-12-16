package org.hulei.springdata.routingdatasource.config.coding;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hulei.springdata.routingdatasource.annotation.DataSourceToggleAop;
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
@Import(value = {FirstHikariDataSourceConfig.class, SecondHikariDataSourceConfig.class})
public class RoutingCodingDataSourceAutoConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSource dataSourceFirst, DataSource dataSourceSecond) {
        return new RoutingCodingDataSource(dataSourceFirst, dataSourceSecond);
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

    @Bean(value = "dataSourceSecond")
    public DataSource dataSourceSecond() {
        HikariConfig config = new HikariDataSource();
        config.setJdbcUrl(secondHikariDataSourceConfig.getUrl());
        config.setUsername(secondHikariDataSourceConfig.getUsername());
        config.setPassword(secondHikariDataSourceConfig.getPassword());
        config.setPoolName(String.format("coding-second-%s", secondHikariDataSourceConfig.getUrl().substring("jdbc:mysql://".length(), secondHikariDataSourceConfig.getUrl().lastIndexOf("?"))));
        return new HikariDataSource(config);
    }
}
