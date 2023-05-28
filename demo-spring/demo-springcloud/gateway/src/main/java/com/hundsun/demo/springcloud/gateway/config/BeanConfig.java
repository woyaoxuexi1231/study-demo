package com.hundsun.demo.springcloud.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundsun.demo.springcloud.gateway.filter.RequestBodyRewrite;
import com.hundsun.demo.springcloud.gateway.filter.RequestTimeFilter;
import com.hundsun.demo.springcloud.gateway.filter.RequestTimeGatewayFilterFactory;
import com.hundsun.demo.springcloud.gateway.resolver.HostAddrKeyResolver;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory;

    // @Autowired
    // LogReqGatewayFilterFactory logReqGatewayFilterFactory;


    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder, ObjectMapper objectMapper) {

        return builder.routes()
                .route( // 创建新路由
                        r -> r.path("/get") // 检查请求路径是否与给定模式匹配的谓词
                                .filters(f -> f.filter(new RequestTimeFilter()).addResponseHeader("X-Response-Default-Foo", "Default-Bar")) // 向路由定义添加筛选器。
                                .uri("http://httpbin.org:80") // 设置路由的 URI。
                                .order(0)
                                .id("customer_filter_router")
                )
                .route( // 创建新路由
                        r -> r.path("/get2") // 检查请求路径是否与给定模式匹配的谓词
                                .filters(f -> f.filter(requestTimeGatewayFilterFactory.apply(new RequestTimeGatewayFilterFactory.Config(true)))) // 向路由定义添加筛选器。
                                .uri("http://httpbin.org:80") // 设置路由的 URI。
                                .order(0)
                                .id("customer_filter_router2")
                )
                // .route( // 创建新路由
                //         r -> r.path("/hi5") // 检查请求路径是否与给定模式匹配的谓词
                //                 .filters(f -> f.filter(someFilter))// 向路由定义添加筛选器。
                //                 .uri("http://localhost:9101") // 设置路由的 URI。
                //                 .order(0)
                //                 .id("customer_filter_router3")
                // )
                .route("path_route_change",
                        r -> r.path("/change")
                                .filters(f -> f
                                        .modifyRequestBody(String.class, String.class, new RequestBodyRewrite(objectMapper))
                                )
                                .uri("http://127.0.0.1:9101"))
                .build();

    }

//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }


    // @Bean
    public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
        return new RequestTimeGatewayFilterFactory();
    }
}
