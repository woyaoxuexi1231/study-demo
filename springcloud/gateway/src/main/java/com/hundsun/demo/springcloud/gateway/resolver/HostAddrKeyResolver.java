package com.hundsun.demo.springcloud.gateway.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * `KeyResolver`是Spring Cloud Gateway中的一个接口，用于在限流、熔断等功能中确定请求的Key。在使用Spring Cloud Gateway进行流量控制时，通常需要根据请求的某些属性来确定请求的唯一标识，从而对请求进行限流或熔断。
 * `KeyResolver`接口的主要作用是根据请求的上下文信息生成一个用于标识请求的Key。这个Key可以基于请求的IP地址、用户ID、请求路径等信息，通常用于在分布式系统中唯一标识一个请求。
 * 实现`KeyResolver`接口需要实现`resolve`方法，该方法接收`ServerWebExchange`对象作为参数，根据请求上下文信息生成并返回一个用于标识请求的Key。例如，可以根据请求的IP地址来生成Key，也可以根据请求的路径和参数来生成Key。
 * 使用`KeyResolver`可以使得限流、熔断等功能更加灵活，可以根据实际需求来确定请求的唯一标识，从而更加精确地控制流量。
 *
 * @author h1123
 * @since 2023/5/7 18:27
 */

@Slf4j
@Component
public class HostAddrKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 这里相当于是把地址作为key返回
        // 这个类配置在 application-limit-router.yml
        // 作用于 RequestRateLimiterGatewayFilterFactory(springcloudgateway内置的) 这个类
        HttpHeaders headers = exchange.getRequest().getHeaders();
        log.info("请求头带出来的信息为: {}", headers);
        String token = headers.getFirst("Token");
        log.info("token: {}", token);
        log.info("当前请求来自: {}", exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        return Mono.just(token);
    }
}
