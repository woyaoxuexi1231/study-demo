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
        Turbine 将多个服务（每个都使用 Hystrix）产生的 实时监控数据流 聚合起来，统一提供给 Hystrix Dashboard 展示。

         Turbine 的工作流程
            1. 各服务通过 Actuator 或 /hystrix.stream 暴露监控数据
            2. Turbine 从注册中心（如 Eureka）中获取所有服务列 表
            3. Turbine 根据配置过滤出启用了 Hystrix 的服务
            4. Turbine 轮询收集这些服务的实时监控数据
            5. 将所有数据合并成一个统一的流，提供给 Dashboard 订阅

        监控配置在 application.yml 文件中

        http://localhost:10004/hystrix
        http://localhost:10004/turbine.stream

         */
        SpringApplication.run(TurbineServiceApplication.class, args);
    }
}
