package com.hundsun.demo.springcloud.gateway.limit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// @Component
public class CustomLocalKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 可以根据需要返回不同的键
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}