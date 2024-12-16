package com.hundsun.demo.springcloud.eureka.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author hulei
 * @since 2023/5/5 20:44
 */

@Deprecated
@EnableHystrixDashboard
@EnableTurbine
@EnableEurekaClient
@SpringBootApplication
public class TurbineServiceApplication {

    public static void main(String[] args) {
        /*
        项目引入 turbine 对 ribbon, eureka-client 模块进行聚合监控

        http://localhost:12013/hystrix
        http://localhost:12013/turbine.stream
         */
        SpringApplication.run(TurbineServiceApplication.class, args);
    }
}
