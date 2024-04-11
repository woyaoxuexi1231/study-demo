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
// /**
//  * GlobalFilter 是 Spring Cloud Gateway 中的一个接口，用于定义全局过滤器。全局过滤器在请求被路由到目标服务之前或之后都可以对请求和响应进行统一的处理。
//  * 与局部过滤器相比，全局过滤器对所有的请求和响应都起作用，不需要显式地将其添加到路由规则中。这使得全局过滤器非常适合于对整个网关流量进行一致性处理，比如请求日志记录、权限验证、异常处理等。
//  * <p>
//  * 所以这里只需要@Component声明成bean就行了,不再需要配置路由规则
//  * <p>
//  * ${@link RequiredArgsConstructor}  会自动生成一个包含所有被声明为 final 以及未初始化的字段的构造函数,spring在没有无参构造器的情况下会使用这个方法构造bean,并完成自动注入
//  */
//
// @Slf4j
// // @Component
// @RequiredArgsConstructor
// public class RequestLoggingFilter implements GlobalFilter, Ordered {
//     /**
//      * ModifyRequestBodyGatewayFilterFactory 是 Spring Cloud Gateway 中的一个过滤器工厂。
//      * 它用于在请求进入网关之前，修改请求体。这是一个有用的功能，因为你可以在将请求转发到后端服务之前，对请求体进行调整，例如添加、删除或修改请求体中的内容。
//      */
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
//                 .apply(modifyRequestBodyGatewayFilterConfig()) // apply 方法用于将一个配置应用到过滤器工厂上，modifyRequestBodyGatewayFilterConfig() 返回的是一个配置对象，用于指定如何修改请求体。
//                 .filter(exchange, chain); // 通过 filter 方法将请求和过滤器链传递给过滤器，以便执行过滤操作。
//     }
//
//     private ModifyRequestBodyGatewayFilterFactory.Config modifyRequestBodyGatewayFilterConfig() {
//         return new ModifyRequestBodyGatewayFilterFactory.Config()
//                 .setRewriteFunction(
//                         String.class, // 表示请求体的类型
//                         String.class, // 表示修改后的请求体的类型
//                         (exchange, body) -> { // exchange 参数代表请求的 ServerWebExchange 对象，body 参数代表原始的请求体内容
//                             // 记录日志
//                             logRequest(exchange.getRequest(), body);
//                             // 返回了一个 Mono 对象，其中包含了修改后的请求体内容
//                             // Mono 对象表示一个异步的操作，最终会产生一个值，这个值就是修改后的请求体内容。
//                             return Mono.just(body);
//                         }
//                 );
//     }
//
//     @Override
//     public int getOrder() {
//         return Ordered.LOWEST_PRECEDENCE;
//     }
// }