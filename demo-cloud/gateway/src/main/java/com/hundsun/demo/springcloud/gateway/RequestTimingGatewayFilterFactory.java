package com.hundsun.demo.springcloud.gateway;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestTimingGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimingGatewayFilterFactory.Config> {

    /*
    GatewayFilter: 过滤器接口，作用于单个路由（Route）。
      1. 如果你只想写一个特定路由专用的过滤逻辑，不需要外部配置参数，那么直接实现 GatewayFilter 就够了。
      2. 你可以手写一个 GatewayFilter 并直接在 路由配置中绑定：
            Route route = RouteLocatorBuilder.routes()
                .route("test_route", r -> r.path("/test/**")
                    .filters(f -> f.filter(new MyCustomGatewayFilter()))
                    .uri("http://example.org"))
                .build();
         或者通过配置类注册（适合全局逻辑）。

    GatewayFilterFactory: 是一个 工厂接口，用于批量创建 GatewayFilter 实例。
    官方内置了大量工厂 AddRequestHeaderGatewayFilterFactory RewritePathGatewayFilterFactory
    特点：
        1. 结合 配置文件（application.yml） 使用。
        2. 允许用户在 application.yml 的 spring.cloud.gateway.routes.filters 下声明配置。
        3. Spring Cloud Gateway 会根据配置自动调用对应的 GatewayFilterFactory 生成 GatewayFilter 并绑定到路由。
    所以这个过滤器工厂可以在配置文件中直接配置 RequestTiming

    如果只是临时写个小过滤器，不需要配置 —— 用 GatewayFilter。
    如果希望过滤器可配置、可复用，并通过 application.yml 管理 —— 用 GatewayFilterFactory。
     */


    // 可以无配置参数，所以使用一个空的 Config 类
    public static class Config {
        // 可以在这里添加配置项，比如是否启用、自定义日志格式等
    }

    public RequestTimingGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 请求进入时间
            long startTime = System.currentTimeMillis();

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // 请求完成时间
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;

                        // 获取请求路径和方法
                        String path = exchange.getRequest().getPath().toString();
                        String method = exchange.getRequest().getMethodValue();

                        log.info("Request [{} {}] took {} ms", method, path, duration);
                    }));
        };
    }
}