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
        /*
        eureka注册页面报错
        1. EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
        Renewals are lesser than threshold：这表明注册到Eureka的服务实例发送的续约（心跳）消息数量低于预设的阈值。在Eureka中，服务实例需要定期发送心跳来表明自己仍然存活。如果在一定时间内接收到的心跳数量低于预期，这通常表明有些服务实例可能已经下线或失去响应。
        Instances are not being expired just to be safe：这意味着尽管心跳数量不足，但Eureka服务器选择不将这些实例标记为下线状态。这是一种安全措施，以防误删仍在运行的实例（可能是因为网络问题或其他原因导致的心跳失败）。



        eureka配置说明：
        eureka.instance.prefer-ip-address=true
           这个配置指示 Eureka 客户端是否应该优先使用 IP 地址而不是主机名来注册自身。
           当设置为 `true` 时，Eureka 客户端会使用 IP 地址来注册和访问服务，而不是使用主机名。
           这对于某些网络环境下，特别是在 Docker 或 Kubernetes 中部署时非常有用。
        eureka.instance.hostname=localhost
           这个配置指定了 Eureka 客户端在注册时使用的主机名。在这个例子中，主机名被设置为 `localhost`。
           如果 `eureka.instance.prefer-ip-address` 被设置为 `true`，那么这个属性的值将被忽略。
        eureka.client.register-with-eureka=false
           这个配置指示 Eureka 客户端是否应该注册自身到 Eureka 服务器。
           在这个例子中，该属性被设置为 `false`，意味着该应用程序不会向 Eureka 服务器注册自己。
        eureka.client.fetch-registry=false
           当该参数设置为 false 时，Eureka 客户端将不会从 Eureka 服务器获取服务注册表。
           换句话说，客户端将不会从 Eureka Server 拉取已注册的服务实例信息。
           适用于 Eureka Server: 因为 Eureka Server 本身负责管理和维护服务注册表，并不需要从其他 Eureka Server 节点中获取注册表信息.
        eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
           用于指定 Eureka 服务器的地址，以便客户端能够注册和发现服务。
           默认情况下，Eureka 客户端会尝试连接 `http://localhost:8761/eureka`，但你可以通过这个参数来自定义 Eureka 服务器的地址。
         */
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
