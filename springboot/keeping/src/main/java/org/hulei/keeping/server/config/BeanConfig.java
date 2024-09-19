package org.hulei.keeping.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/12 2:12
 */
@Configuration
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
