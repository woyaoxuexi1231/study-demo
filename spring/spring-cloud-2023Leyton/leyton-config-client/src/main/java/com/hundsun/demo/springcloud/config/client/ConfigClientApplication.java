package com.hundsun.demo.springcloud.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientApplication {

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {

        SpringApplication.run(ConfigClientApplication.class, args);
    }
}
