package com.hundsun.demo.springcloud.eureka.zuul.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author hulei
 * @since 2023/5/5 20:44
 */


@EnableZuulProxy
@EnableHystrix
@EnableHystrixDashboard
@Deprecated
@EnableEurekaClient
@SpringBootApplication
public class EurekaZuulClientApplication {

    public static void main(String[] args) {
        /*
        Spring Cloud 的 Zuul 是一个基于 Netflix Zuul 实现的 微服务网关组件，它的主要作用是为后端服务提供统一的访问入口，进行路由转发、请求过滤、权限验证、限流等功能。
            路由转发（Routing）	根据请求路径将请求转发到不同的微服务
            负载均衡	搭配 Ribbon 进行客户端负载均衡
            请求过滤（Filter）	在请求前后执行逻辑，如鉴权、日志、限流等
            熔断容错	与 Hystrix 集成，在服务调用失败时提供 fallback
            动态路由	可动态修改路由规则
            安全控制	统一身份验证和权限管理，如 JWT 校验
         */
        SpringApplication.run(EurekaZuulClientApplication.class, args);
    }
}
