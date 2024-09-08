package com.hundsun.demo.springcloud.eureka.ribbon.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@Deprecated
// 开启熔断器监控
@EnableHystrixDashboard
// 开启熔断器
@EnableHystrix
@EnableEurekaClient
@SpringBootApplication
public class EurekaRibbonClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaRibbonClientApplication.class, args);
    }
}
