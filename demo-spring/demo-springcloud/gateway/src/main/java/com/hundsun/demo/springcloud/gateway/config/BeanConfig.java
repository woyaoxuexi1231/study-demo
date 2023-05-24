package com.hundsun.demo.springcloud.gateway.config;

import com.hundsun.demo.springcloud.gateway.filter.RequestTimeFilter;
import com.hundsun.demo.springcloud.gateway.filter.RequestTimeGatewayFilterFactory;
import com.hundsun.demo.springcloud.gateway.resolver.HostAddrKeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.gateway.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 18:35
 */

@Configuration
public class BeanConfig {

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver() {
        return new HostAddrKeyResolver();
    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/get")
                        .filters(f -> f.filter(new RequestTimeFilter())
                                .addResponseHeader("X-Response-Default-Foo", "Default-Bar"))
                        .uri("http://httpbin.org:80")
                        .order(0)
                        .id("customer_filter_router")
                )
                .build();

    }

//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }


    @Bean
    public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }
}
