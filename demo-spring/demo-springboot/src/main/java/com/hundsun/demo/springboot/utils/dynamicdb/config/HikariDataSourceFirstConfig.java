package com.hundsun.demo.springboot.utils.dynamicdb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: HikariDataSourceConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 18:17
 */

@ConfigurationProperties(prefix = "spring.datasource.first")
@Component
@Data
public class HikariDataSourceFirstConfig {

    private String url;

    private String username;

    private String password;

}
