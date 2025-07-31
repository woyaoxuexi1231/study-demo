package org.hulei.springdata.routingdatasource.config.coding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.datasource.second")
@Component
@Data
public class HikariDataSourceSecondConfig {

    private String url;

    private String username;

    private String password;

}