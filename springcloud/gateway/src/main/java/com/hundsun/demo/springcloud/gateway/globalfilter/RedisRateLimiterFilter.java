package com.hundsun.demo.springcloud.gateway.globalfilter;

import cn.hutool.core.lang.Pair;
import com.hundsun.demo.springcloud.gateway.config.RedisLimit;
import com.hundsun.demo.springcloud.gateway.config.RedisLimitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author hulei
 * @since 2024/10/23 10:39
 */

@Slf4j
// @Component
public class RedisRateLimiterFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    private static final int LIMIT = 10; // 请求限制
    private static final int WINDOW_SIZE = 60; // 时间窗口，单位为秒
    private static final String STRING_PRE_FIX = "limit::api::";

    public boolean isAllowed(String ip) {
        Long increment = stringRedisTemplate.opsForValue().increment(STRING_PRE_FIX + ip);
        if (Objects.isNull(increment)) {
            throw new RuntimeException("redis操作问题");
        }
        if (increment == 1) {
            // 设置过期时间
            stringRedisTemplate.expire(STRING_PRE_FIX + ip, WINDOW_SIZE, TimeUnit.SECONDS);
        }
        return increment <= LIMIT;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取机器的IP
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        String path = exchange.getRequest().getPath().value();

        if (!isAllowed(ip)) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        // 继续处理请求
        System.out.println("Request allowed");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Autowired
    RedisLimitConfig redisLimitConfig;

    Map<Pair<String, String>, RedisLimit> limitMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initLimitConfig() {
        if (CollectionUtils.isEmpty(redisLimitConfig.getRedisLimits())) {
            return;
        }
        for (RedisLimit redisLimit : redisLimitConfig.getRedisLimits()) {
            Pair<String, String> pair = new Pair<>(redisLimit.getApiName(), redisLimit.getUserName());
            limitMap.put(pair, redisLimit);
        }
    }
}
