package com.hundsun.demo.springcloud.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

// @Component
public class SomeFilter implements GlobalFilter, Ordered {

    private final ModifyRequestBodyGatewayFilterFactory factory;

    @Autowired
    public SomeFilter(ModifyRequestBodyGatewayFilterFactory factory) {
        this.factory = factory;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Map<String, String> someMap = new HashMap<>();
        someMap.put("reqString", "100");

        ModifyRequestBodyGatewayFilterFactory.Config cfg = new ModifyRequestBodyGatewayFilterFactory.Config();
        cfg.setRewriteFunction(String.class, String.class, new SomeRewriteFunction(someMap));

        GatewayFilter modifyBodyFilter = factory.apply(cfg);

        return modifyBodyFilter.filter(exchange, ch -> Mono.empty())
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}