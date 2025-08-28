package com.hundsun.demo.springcloud.gateway.ratelimit.def;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomLocalKeyResolver implements KeyResolver {

    private final String routeKey;

    public CustomLocalKeyResolver(String routeKey) {
        this.routeKey = routeKey;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        /*
        KeyResolver 接口允许使用可插拔策略来导出用于限制请求的 key。
        KeyResolver 的默认实现是 PrincipalNameKeyResolver，它从 ServerWebExchange 中检索 Principal 并调用 Principal.getName()
        默认情况下，如果 KeyResolver 未找到 key，则请求将被拒绝。
        可以通过设置 spring.cloud.gateway.filter.request-rate-limiter.deny-empty-key（true 或 false）和 spring.cloud.gateway.filter.request-rate-limiter.empty-key-status-code 属性来调整此行为。

        KeyResolver 在被使用在 RedisRateLimiter 时，用作redis限流键的依据
        比如仅仅使用ip，那么redis生成 request_rate_limiter.{ip}.tokens  request_rate_limiter.{ip}.timestamp
        这样无法针对路由或者路径进行限流，所以最好加点额外的信息
         */
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        return Mono.just(routeKey + ":" + ip);

    }
}