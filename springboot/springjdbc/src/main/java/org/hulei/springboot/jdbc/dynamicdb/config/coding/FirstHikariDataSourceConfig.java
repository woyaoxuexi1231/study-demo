package org.hulei.springboot.jdbc.dynamicdb.config.coding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.datasource.first")
@Component
@Data
public class FirstHikariDataSourceConfig {

    private String url;

    private String username;

    private String password;

}