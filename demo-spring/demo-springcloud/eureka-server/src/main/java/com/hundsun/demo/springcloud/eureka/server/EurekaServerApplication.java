package com.hundsun.demo.springcloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

/*
`@EnableEurekaServer` 是 Spring Cloud 中的一个注解，用于启用 Eureka 服务器的功能。Eureka 是 Netflix 提供的一个服务注册和发现组件，用于构建具有高可用性的分布式系统。
当你在 Spring Cloud 应用程序的启动类上添加 `@EnableEurekaServer` 注解时，Spring Boot 应用程序会启动一个 Eureka 服务器实例，该实例将用于服务注册和发现。这意味着你可以将该应用程序作为 Eureka 服务器来管理服务的注册和发现，其他微服务应用程序可以将自己注册到该 Eureka 服务器上，并从中获取其他服务的信息。
使用 `@EnableEurekaServer` 注解，你可以轻松地将一个普通的 Spring Boot 应用程序转换为 Eureka 服务器，从而实现基于 Eureka 的服务注册和发现功能。这对于构建具有弹性和可扩展性的微服务架构非常有用。
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
