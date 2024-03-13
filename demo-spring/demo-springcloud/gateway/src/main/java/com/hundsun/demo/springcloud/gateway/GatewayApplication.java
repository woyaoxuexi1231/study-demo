package com.hundsun.demo.springcloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@EnableEurekaClient
@Slf4j
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {

        /*
        DiscoveryClient_EUREKA-CLIENT/LAPTOP-HGITO649:eureka-client:9101 - registration status: 204
        注册成功
         */
        SpringApplication.run(GatewayApplication.class, args);
    }
}
