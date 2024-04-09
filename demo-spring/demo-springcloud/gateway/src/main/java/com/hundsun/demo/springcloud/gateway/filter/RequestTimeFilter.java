// package com.hundsun.demo.springcloud.gateway.filter;
//
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.core.Ordered;
// import org.springframework.web.server.ServerWebExchange;
// import reactor.core.publisher.Mono;
//
// /**
//  * @projectName: study-demo
//  * @package: com.hundsun.demo.springcloud.gateway.filter
//  * @className: RequestTimeFilter
//  * @description:
//  * @author: hl
//  * @createDate: 2023/5/25 0:35
//  */
//
// @Slf4j
// public class RequestTimeFilter implements GatewayFilter, Ordered {
//
//     private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
//
//     /**
//      * 处理 Web 请求并（可选）通过给定的网关过滤器链委派给下一个 WebFilter。
//      *
//      * @param exchange the current server exchange
//      * @param chain    provides a way to delegate to the next filter
//      * @return
//      */
//     @Override
//     public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//         exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
//         return chain.filter(exchange) // 委派给链中的下一个 Web 筛选器。
//                 .then(  // Let this Mono complete then play another Mono.
//                         // 创建一个 Mono，一旦执行了提供的 Runnable 完成，该 Mono 就会为空。
//                         Mono.fromRunnable(() -> {
//                             Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
//                             if (startTime != null) {
//                                 log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
//                             }
//                         })
//                 );
//
//     }
//
//     @Override
//     public int getOrder() {
//         return 0;
//     }
// }
