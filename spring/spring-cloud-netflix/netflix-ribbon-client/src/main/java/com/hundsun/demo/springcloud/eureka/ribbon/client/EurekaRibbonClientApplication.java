package com.hundsun.demo.springcloud.eureka.ribbon.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author hulei
 * @since 2023/5/5 20:44
 */

@Deprecated
/*
Ribbon 是一个客户端负载均衡器（Client-side Load Balancer）

1. 客户端负载均衡
Ribbon 会从注册中心（如 Eureka）获取服务实例列表，然后客户端在本地根据某种策略（如轮询、随机等）选择一个实例进行请求。

区别于服务端负载均衡（如 Nginx）：Ribbon 是客户端决定请求哪台服务。

2. 服务实例选择策略
默认是 轮询（RoundRobinRule）。

支持多种策略：
    RandomRule：随机选择一个服务实例
    RetryRule：在指定时间内重试选中的服务实例
    BestAvailableRule：选择并发量最小的实例
    ZoneAvoidanceRule：结合区域和实例性能选择最佳节点

3. 与 RestTemplate、Feign 集成
可配合 @LoadBalanced 注解的 RestTemplate 使用，实现通过服务名调用其他服务。

Feign 底层也使用 Ribbon 来实现客户端负载均衡。



==============================================================

开启熔断器监控
Spring Cloud 2.0及以上版本对Hystrix的配置发生了变化。在这些版本中，/hystrix.stream 路径被替换为 /actuator/hystrix.stream

1. 打开监控页面
http://localhost:10003/hystrix
2. 输出监控流, 可视化页面就可以监控当前服务
http://localhost:10003/actuator/hystrix.stream
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
