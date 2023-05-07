package com.hundsun.demo.springcloud.gateway.resolver;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.gateway.resolver
 * @className: HostAddrKeyResolver
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 18:27
 */

public class HostAddrKeyResolver implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
