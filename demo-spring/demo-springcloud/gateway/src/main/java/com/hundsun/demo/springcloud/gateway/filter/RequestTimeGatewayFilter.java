package com.hundsun.demo.springcloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 打印请求耗时的过滤器
 * 这是一个自定义的过滤器
 *
 * @author hl
 * @since 2023/5/25 0:35
 */

@Slf4j
public class RequestTimeGatewayFilter implements GatewayFilter, Ordered {

    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    /**
     * 处理 Web 请求并（可选）通过给定的网关过滤器链委派给下一个 WebFilter。
     *
     * @param exchange ServerWebExchange 是 Spring WebFlux 中的核心接口之一，它代表了一个正在处理的 HTTP 请求和响应交换对象。在Spring Cloud Gateway中，每个传入的请求都会被封装为一个 ServerWebExchange 对象，然后被传递给网关的过滤器链进行处理。
     *                 ServerWebExchange 包含了与 HTTP 请求和响应相关的各种信息，如请求头、响应状态码、请求参数等。通过 ServerWebExchange，可以对请求和响应进行修改、访问相关属性以及获取有关请求的信息。
     * @param chain    GatewayFilterChain 是 Spring Cloud Gateway 中用于管理过滤器的接口。过滤器链由一系列的 GatewayFilter 组成，它们被按顺序应用于传入的请求，每个过滤器可以修改请求、响应或者进行一些其他的操作。
     *                 当一个请求到达网关时，会被传递给 GatewayFilterChain，然后 GatewayFilterChain 会依次调用过滤器链中的每个过滤器来处理请求。过滤器链的最后一个元素通常是一个终止过滤器，它将请求转发给实际的目标服务。
     * @return mono 在 Spring WebFlux 中，Mono<Void> 通常用于表示一个 WebFlux Handler 或 WebFlux Filter 中的最终处理结果，用于指示请求处理已完成，可以发送响应给客户端了。
     * 在网关或过滤器中，当使用 Mono<Void> 时，通常表示一个异步操作的完成状态，例如在请求处理完成后执行一些额外的操作，如日志记录、统计信息收集等。
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 这行代码将当前时间戳（以毫秒为单位）存储在 WebFlux 的 ServerWebExchange 对象的属性中。REQUEST_TIME_BEGIN 用于标识存储时间戳的属性键。
        // 这里相当于一个pre类型的过滤器,在请求开始前记录时间
        exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
        // 这里是一个典型的 WebFlux 过滤器链的调用。chain.filter(exchange) 表示将当前的 ServerWebExchange 对象传递给过滤器链中的下一个过滤器进行处理。
        return chain.filter(exchange)
                // 这一部分是一个响应式 Mono 操作符，表示在当前操作完成后执行提供的 Runnable。换句话说，它会在上述过滤器链执行完毕后，执行括号内提供的操作。
                .then(
                        // 创建一个 Mono，该 Mono 会在执行了提供的 Runnable 后完成。这个 Runnable 实际上是一个 lambda 表达式，用于记录请求处理时间的逻辑。
                        Mono.fromRunnable(() -> {
                            // 这个run方法相当于post类型的过滤器,会在请求调用完成,返回响应之前调用这个run方法
                            // 从 ServerWebExchange 对象的属性中获取之前存储的请求开始时间戳。
                            Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                            if (startTime != null) {
                                // 如果成功获取了请求开始时间戳，则计算当前时间与请求开始时间的差值，以获取请求处理时间。然后将请求路径和处理时间以日志的形式输出。
                                log.info(exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                            }
                        })
                );

    }

    /**
     * 设置过滤器的优先级,还是老样子,值越小,优先级越高
     *
     * @return 0
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
