package com.hundsun.demo.springcloud.eureka.zuul.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@Deprecated
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class EurekaZuulClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaZuulClientApplication.class, args);
    }
}
