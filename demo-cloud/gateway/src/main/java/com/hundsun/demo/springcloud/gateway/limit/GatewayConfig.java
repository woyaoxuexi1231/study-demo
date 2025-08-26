// package com.hundsun.demo.springcloud.gateway.limit;
//
// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
// import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
// import reactor.core.publisher.Mono;
//
// // @Configuration
// public class GatewayConfig {
//
//     // 定义KeyResolver Bean
//     @Bean
//     public KeyResolver hostAddrKeyResolver() {
//         return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
//     }
//
//     // 定义RedisRateLimiter Bean
//     @Bean
//     public RedisRateLimiter redisRateLimiter() {
//         return new RedisRateLimiter(1, 3);
//     }
//
//     // 定义RouteLocator Bean
//     @Bean
//     public RouteLocator customRouteLocator(RouteLocatorBuilder builder, RedisRateLimiter rateLimiter) {
//         return builder.routes()
//             .route("limit_route", r -> r.path("/get")
//                 .and()
//                 .after(java.time.ZonedDateTime.parse("2017-01-20T17:42:47.789-07:00[America/Denver]"))
//                 .filters(f -> f.requestRateLimiter(conf -> conf.setRateLimiter(rateLimiter)
//                     .setKeyResolver(hostAddrKeyResolver())))
//                 .uri("http://httpbin.org:80"))
//             .build();
//     }
// }