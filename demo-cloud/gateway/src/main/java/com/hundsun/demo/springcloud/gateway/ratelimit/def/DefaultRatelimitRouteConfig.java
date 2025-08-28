package com.hundsun.demo.springcloud.gateway.ratelimit.def;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;

@Configuration
public class DefaultRatelimitRouteConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        /*
        Redis 实现基于 Stripe 完成的工作。它需要使用 spring-boot-starter-data-redis-reactive Spring Boot starter。
        使用的算法是令牌桶算法。

        https://docs.springframework.org.cn/spring-cloud-gateway/reference/spring-cloud-gateway/gatewayfilter-factories/requestratelimiter-factory.html

        replenishRate 每秒向“桶”里放入多少个新令牌。
        burstCapacity 桶最多能存放多少個令牌。这个值决定了系统处理突发流量的能力。
        requestedTokens 属性是每个请求消耗多少令牌。这是从令牌桶中为每个请求取出的令牌数量，默认为 1。

        RedisRateLimiter 要以 bean 形式，内部有生命周期，单纯 new 的话比较麻烦，就要自己管理生命周期了
        但是在使用 RequestRateLimiterGatewayFilterFactory 的时候，只能有一个 RateLimiter，所以要考虑到底要使用自定义，还是用默认的
        lua 脚本为 request_rate_limiter.lua, 在spring-cloud-gateway-server这个包内
         */
        return new RedisRateLimiter(1, 10, 10);
    }

    @Bean
    public RouteLocator defaultRatelimitRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("default_ratelimit_route",
                        r -> r.path("/netflix-provider/hi-default-rate-limit")
                                .filters(f -> f
                                        .requestRateLimiter(conf -> conf.setRateLimiter(redisRateLimiter()).setKeyResolver(new CustomLocalKeyResolver("default_ratelimit_route"))) // spring cloud gateway 自带的限流器配置
                                        .stripPrefix(1)  // 去掉 1 个前缀，/netflix-provider将被去掉
                                )
                                .uri("lb://NETFLIX-PROVIDER"))
                .build();

    }
}