package com.hundsun.demo.springcloud.eureka.feign.client.service;

import com.hundsun.demo.springcloud.eureka.feign.client.config.FeignConfig;
import com.hundsun.demo.springcloud.eureka.feign.client.service.impl.HiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 一个 Feign 客户端
 * Feign 客户端是用于实现服务间通信的一种方式，它是 Spring Cloud 中的一个组件，基于接口定义和注解方式来定义和调用 RESTful 服务。
 * Feign 客户端使得编写 HTTP 客户端变得更加简单和直观，不需要手动创建 HTTP 请求，而是通过定义接口和注解的方式来声明和调用服务接口，框架会自动帮助你生成具体的 HTTP 请求并发送到目标服务。
 * Feign 客户端的主要作用包括：
 * 1. 简化服务调用通过 Feign 客户端，你可以用接口的方式来声明服务间的调用，而不需要手动编写 HTTP 请求和处理响应。这样大大简化了服务调用的流程和代码编写。
 * 2. 声明式 REST 客户端**：Feign 客户端支持使用注解的方式来定义接口和声明服务调用的各种属性，比如请求路径、请求方法、请求头、请求体等。这样可以让你更加直观和灵活地定义和配置服务调用的方式。
 * 3. 集成了负载均衡和断路器**：Feign 客户端集成了 Ribbon 负载均衡和 Hystrix 断路器，可以通过注解的方式来配置负载均衡策略和断路器的行为。这样可以让你更容易地实现服务调用的负载均衡和容错处理。
 * 4. 与服务注册中心集成**：Feign 客户端与 Spring Cloud 的服务注册中心集成，可以直接通过服务名来调用服务，而无需手动维护服务的地址和端口信息。
 * 总的来说，Feign 客户端使得编写和调用 RESTful 服务变得更加简单和直观，提高了开发效率，并且提供了负载均衡、断路器等强大的功能，帮助开发者构建健壮的微服务架构。
 *
 * @author hulei
 * @since 2023/5/7 13:17
 */

/*
@FeignClient 声明 Feign 客户端的注解。它用于创建一个 Feign 客户端，并指定要调用的服务名称、自定义的配置、以及断路器（Hystrix）的回退实现。
1. value(name): 指定要调用的服务名称。这个服务名称对应于 Eureka 注册中心中注册的服务名，Feign 客户端将根据这个服务名来查找服务实例并进行调用。
2. configuration: 指定自定义的 Feign 配置类。你可以在这个配置类中配置 Feign 客户端的相关参数，比如超时时间、重试策略等。
3. fallback: 指定断路器（Hystrix）的回退实现类。如果调用服务失败或者出现异常，断路器会启用，并调用指定的回退实现类来处理回退逻辑。
4. url: 配置服务的地址
 */
@FeignClient(
        name = "cloud-client",
        // url = "http://localhost:12008", // 这里暂时不配置, 我们通过eureka注册中心拿到服务
        configuration = FeignConfig.class,
        fallback = HiHystrix.class)
@Service
public interface CloudClientFeign {

    @GetMapping("/hi")
    String sayHiFromClientEureka();

    @GetMapping("/hi2")
    String sayHi2FromClientEureka(@RequestParam(required = false, name = "req") String req, @RequestParam(required = false, name = "other") String other);
}
