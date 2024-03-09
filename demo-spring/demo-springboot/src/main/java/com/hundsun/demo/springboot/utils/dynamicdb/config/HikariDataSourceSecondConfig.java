package com.hundsun.demo.springboot.utils.dynamicdb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: HikariDataSourceSecondConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 18:22
 */

@ConfigurationProperties(prefix = "spring.datasource.second")
@Component
@Data
public class HikariDataSourceSecondConfig {

    private String url;

    private String username;

    private String password;

}
