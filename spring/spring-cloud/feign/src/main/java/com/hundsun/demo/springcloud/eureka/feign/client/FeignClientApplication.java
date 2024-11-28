package com.hundsun.demo.springcloud.eureka.feign.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hulei
 * @since  2023/5/5 20:44
 */


// 启动 Feign
@EnableFeignClients
// 开启服务发现
@EnableDiscoveryClient
@SpringBootApplication
public class FeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }
}
