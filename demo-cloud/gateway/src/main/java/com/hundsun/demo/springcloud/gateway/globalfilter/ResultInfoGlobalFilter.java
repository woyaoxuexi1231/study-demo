// package com.hundsun.demo.springcloud.gateway.filter;
//
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
// import org.springframework.core.Ordered;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferFactory;
// import org.springframework.http.server.reactive.ServerHttpResponse;
// import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
//
// import java.nio.charset.StandardCharsets;
//
// /**
//  * @author hulei
//  * @since 2024/10/23 13:23
//  */
//
// @Component
// public class ResultInfoGlobalFilter implements GlobalFilter, Ordered {
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//         return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//             ServerHttpResponse originalResponse = exchange.getResponse();
//             DataBufferFactory bufferFactory = originalResponse.bufferFactory();
//
//             ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
//
//             };
//
//             return chain.filter(exchange.mutate().response(decoratedResponse).build());
//         }));
//     }
//
//     @Override
//     public int getOrder() {
//         return 0;
//     }
// }
