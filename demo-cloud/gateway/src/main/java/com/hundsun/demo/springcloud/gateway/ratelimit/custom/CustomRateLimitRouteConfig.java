package com.hundsun.demo.springcloud.gateway.ratelimit.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hulei
 * @since 2025/8/28 13:37
 */

@Configuration
public class CustomRateLimitRouteConfig {

    @Autowired
    CustomLocalRateLimiterFilter customLocalRateLimiterFilter;

    @Autowired
    CustomRedisRateLimiterFilter customRedisRateLimiterFilter;

    @Bean
    public RouteLocator customRatelimitRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // 配置 CustomLocalRateLimiterFilter 路由
                .route("custom-local-ratelimit",
                        r -> r.path("/netflix-provider/hi-custom-local-ratelimit")
                                .filters(f -> f
                                        .filter(customLocalRateLimiterFilter)
                                        .stripPrefix(1)
                                )
                                .uri("lb://NETFLIX-PROVIDER"))

                // 配置 CustomRedisRateLimiterFilter 路由
                .route("custom-redis-ratelimit",
                        r -> r.path("/netflix-provider/hi-custom-redis-ratelimit")
                                .filters(f -> f
                                        .filter(customRedisRateLimiterFilter)
                                        .stripPrefix(1)
                                )
                                .uri("lb://NETFLIX-PROVIDER"))

                .build();

    }
}
