package com.hundsun.demo.springcloud.gateway;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hundsun.demo.springcloud.gateway.config.RedisLimitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@RestController
@EnableDiscoveryClient
@Slf4j
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {

        /*
        Spring Cloud Gateway 是基于 Spring WebFlux 和 Reactor 模型构建的一个异步非阻塞 API 网关
        它的核心功能是：请求路由、过滤器链处理、服务发现和负载均衡等。

         */
        SpringApplication.run(GatewayApplication.class, args);
    }

    // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("add_request_parameter_route",
    //                     r -> r.path("/eureka-client/**")
    //                             .filters(f -> f.addRequestParameter("paramName", "paramValue"))
    //                             .uri("lb://eureka-client"))
    //             .build();
    // }

    @SentinelResource(value = "yourApi",
            blockHandler = "handleBlock")
    @GetMapping("/hi")
    public String hi() {
        return "hello world";
    }

    // 限流后的处理方法（参数、返回值需与原方法相同）
    public String handleBlock(BlockException ex) {
        return "Request has been blocked!";
    }

    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RedisLimitConfig redisLimitConfig;

    @GetMapping("/test")
    public void test() {
        log.info("{}", redisLimitConfig.getRedisLimits());
    }
}
