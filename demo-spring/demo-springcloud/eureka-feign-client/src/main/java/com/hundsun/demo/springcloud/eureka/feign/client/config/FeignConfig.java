package com.hundsun.demo.springcloud.eureka.feign.client.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.config
 * @className: FeignConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 13:18
 */

@Configuration
public class FeignConfig {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
    }
}
