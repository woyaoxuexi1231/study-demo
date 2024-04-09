// package com.hundsun.demo.springcloud.gateway.filter;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
// import org.springframework.core.Ordered;
// import org.springframework.http.server.reactive.ServerHttpRequest;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// @Slf4j
// // @Component
// @RequiredArgsConstructor
// public class RequestLoggingFilter implements GlobalFilter, Ordered {
//
//     private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;
//
//     private static void logRequest(final ServerHttpRequest request, String body) {
//         log.info("============ Request Start Id: {} ============", request.getId());
//         log.info("Request Id: {}, URI: {}", request.getId(), request.getURI());
//         log.info("Request Id: {}, Headers: {}", request.getId(), request.getHeaders());
//         log.info("Request Id: {}, QueryParams: {}", request.getId(), request.getQueryParams());
//         log.debug("Request Id: {}, Body: {}", request.getId(), body);
//         log.info("============ Request End Id: {} ============", request.getId());
//     }
//
//     @Override
//     public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
//         return modifyRequestBodyGatewayFilterFactory
//                 .apply(modifyRequestBodyGatewayFilterConfig())
//                 .filter(exchange, chain);
//     }
//
//     private ModifyRequestBodyGatewayFilterFactory.Config modifyRequestBodyGatewayFilterConfig() {
//         return new ModifyRequestBodyGatewayFilterFactory.Config()
//                 .setRewriteFunction(String.class, String.class, (exchange, body) -> {
//                             logRequest(exchange.getRequest(), body);
//                             return Mono.just(body);
//                         }
//                 );
//     }
//
//     @Override
//     public int getOrder() {
//         return Ordered.HIGHEST_PRECEDENCE;
//     }
// }