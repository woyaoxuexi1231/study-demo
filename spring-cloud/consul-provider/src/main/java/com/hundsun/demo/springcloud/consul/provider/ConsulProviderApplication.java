package com.hundsun.demo.springcloud.consul.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author h1123
 * @since 2023/5/5 20:44
 */

// 开启服务注册发现
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulProviderApplication {

    public static void main(String[] args) {

        /*
        DiscoveryClient_EUREKA-CLIENT/LAPTOP-HGITO649:eureka-client:9101 - registration status: 204
        注册成功
         */
        SpringApplication.run(ConsulProviderApplication.class, args);
    }
}
