package org.hulei.springdata.routingdatasource.config.coding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.datasource.first")
@Component
@Data
public class HikariDataSourceFirstConfig {

    private String url;

    private String username;

    private String password;

}