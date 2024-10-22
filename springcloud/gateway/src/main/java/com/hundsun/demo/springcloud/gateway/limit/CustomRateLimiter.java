package com.hundsun.demo.springcloud.gateway.limit;

import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// @Component
public class CustomRateLimiter implements RateLimiter<Object> {

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        // 实现限流逻辑
        return Mono.just(new Response(true, new HashMap<>()));
    }

    @Override
    public Map<String, Object> getConfig() {
        return Collections.emptyMap();
    }

    @Override
    public Class<Object> getConfigClass() {
        return null;
    }

    @Override
    public Object newConfig() {
        return null;
    }
}