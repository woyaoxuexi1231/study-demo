package com.hundsun.demo.springcloud.eureka.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@Deprecated
@EnableHystrixDashboard
@EnableTurbine
@EnableEurekaClient
@SpringBootApplication
public class EurekaMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMonitorApplication.class, args);
    }
}
