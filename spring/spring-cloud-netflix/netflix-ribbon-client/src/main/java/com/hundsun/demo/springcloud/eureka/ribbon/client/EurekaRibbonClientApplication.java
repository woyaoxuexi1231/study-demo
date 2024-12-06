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
/*
开启熔断器监控
Spring Cloud 2.0及以上版本对Hystrix的配置发生了变化。在这些版本中，/hystrix.stream 路径被替换为 /actuator/hystrix.stream

1. 打开监控页面
http://localhost:12011/hystrix
2. 输出监控流, 可视化页面就可以监控当前服务
http://localhost:12011/actuator/hystrix.stream
 */
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
