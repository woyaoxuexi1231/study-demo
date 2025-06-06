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
        /*
        Feign 是对 REST 请求的封装，让你用接口 + 注解的方式调用远程服务，不用写 HttpClient 或 RestTemplate 的繁琐代码。
         */
        SpringApplication.run(FeignClientApplication.class, args);
    }
}
