package com.hundsun.demo.springcloud.gateway.ratelimit.custom;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomLocalRateLimiterFilter implements GatewayFilter, Ordered {

    // 这里我们使用内存存储请求计数。对于生产环境，最好选用分布式计数器（比如 Redis）。
    private final ConcurrentMap<String, AtomicInteger> countMap = new ConcurrentHashMap<>();
    private final int limit = 5; // 每分钟请求限制
    private final long windowSize = 10000; // 时间窗口大小（毫秒）
    /**
     *
     */
    private final ConcurrentMap<String, Long> timestampMap = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取机器的IP
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        // 当前时间
        Long currentTime = System.currentTimeMillis();
        // 替换有效的时间
        // 1. 如果ip的时间为空,以当前时间为新的时间
        // 2. 如果ip的时间不为空,那么根据是否超过时间限制来设置时间,并且更新计数器
        timestampMap.compute(ip, (key, timestamp) -> {
            if (timestamp == null || (currentTime - timestamp) > windowSize) {
                countMap.put(key, new AtomicInteger(0));
                return currentTime;
            }
            return timestamp;
        });

        // 得到当前的计数值
        AtomicInteger requestCount = countMap.computeIfAbsent(ip, k -> new AtomicInteger(0));


        if (requestCount.incrementAndGet() > limit) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 让这个过滤器在处理请求之前优先执行
    }
}