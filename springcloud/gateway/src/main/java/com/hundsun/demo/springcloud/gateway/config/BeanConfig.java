package com.hundsun.demo.springcloud.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.gateway.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 18:35
 */

@Slf4j
@Configuration
public class BeanConfig {

    // @Autowired
    // LogReqGatewayFilterFactory logReqGatewayFilterFactory;

    /**
     * 构造路由规则
     * `RouteLocator` 是 Spring Cloud Gateway 中的一个接口，用于定义路由信息。在网关应用中，路由是指将传入的请求映射到后端服务的规则。`RouteLocator` 接口提供了一种灵活的方式来配置路由规则，可以通过代码或者配置文件来定义路由信息。
     * 通常情况下，开发人员可以使用 `RouteLocator` 来定义一系列的路由规则，包括匹配的路径、目标服务的 URL、过滤器等。这些路由规则可以根据请求的路径、方法、请求头等信息来进行匹配，并将请求转发到相应的目标服务上。
     * Spring Cloud Gateway 提供了多种实现 `RouteLocator` 接口的方式，包括基于 Java 代码的配置、基于 YAML 或者 properties 文件的配置等。开发人员可以根据具体的需求选择合适的方式来定义路由规则。
     *
     * @param builder 路由构造器类
     * @return routerLocator
     */
    // @Bean
    // public RouteLocator requestTimeFilterRouteLocator(RouteLocatorBuilder builder) {
    //     // ObjectMapper objectMapper
    //     return builder.routes()
    //             .route( // 创建新路由,对于请求路径为 “/get” 的请求，只有第一个路由规则会被匹配到并执行，即使有第二个路由规则也匹配路径为 “/get”。因此，第二个路由规则将被忽略，不会被执行。
    //                     r -> r.path("/get") // 表示这个路由规则会匹配请求路径为 “/get” 的请求
    //                             .filters(f -> f.filter(new RequestTimeGatewayFilter())
    //                                     .addRequestHeader("X-Response-Default-Foo", "Default-Bar") // 在转发请求的响应中添加了一个名为 X-Response-Default-Foo 值为 Default-Bar 的头信息
    //                                     .addRequestParameter("add-something", "hahaha") // 直接添加一个参数
    //                             )
    //                             .uri("http://httpbin.org:80") // 设置路由的 URI。
    //             )
    //             .build();
    //
    // }
    //
    // @Bean
    // public RouteLocator requestTimeFilterFactoryLocator(RouteLocatorBuilder builder, RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory) {
    //     return builder.routes()
    //             .route( // 创建新路由
    //                     r -> r.path("/ip") // 检查请求路径是否与给定模式匹配的谓词
    //                             .filters(f -> f.filter(requestTimeGatewayFilterFactory.apply(new RequestTimeGatewayFilterFactory.Config(true, "hello", "world")))) // 向路由定义添加筛选器。
    //                             .uri("http://httpbin.org:80") // 设置路由的 URI。
    //             )
    //             .build();
    // }
    //
    //
    // // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("service_route", r -> r
    //                     .path("/eureka-client/**") // 匹配请求路径
    //                     .filters(f -> f.stripPrefix(1)) // 去掉 eureka-client 这个路径
    //                     .uri("lb://EUREKA-CLIENT") // 转发到Eureka中名为SERVICE-NAME的服务
    //             )
    //             .build();
    // }


    // @Bean
    // public RouteLocator requestLoggingFilterLocator(RouteLocatorBuilder builder) {
    //     // return builder.routes()
    //     //         .route( // 创建新路由
    //     //                 r -> r.path("/get") // 检查请求路径是否与给定模式匹配的谓词
    //     //                         .filters(f -> f.filter(requestLoggingFilter)) // 向路由定义添加筛选器。
    //     //                         .uri("http://httpbin.org:80") // 设置路由的 URI。
    //     //         )
    //     //         .build();
    // }


    // // .route( // 创建新路由
    // //         r -> r.path("/hi5") // 检查请求路径是否与给定模式匹配的谓词
    // //                 .filters(f -> f.filter(someFilter))// 向路由定义添加筛选器。
    // //                 .uri("http://localhost:9101") // 设置路由的 URI。
    // //                 .order(0)
    // //                 .id("customer_filter_router3")
    // // )
    // .route("path_route_change",
    //         r -> r.path("/change")
    //                 .filters(f -> f
    //                         .modifyRequestBody(String.class, String.class, new RequestBodyRewrite(objectMapper))
    //                 )
    //                 .uri("http://127.0.0.1:9101"))

//    @Bean
//    public TokenFilter tokenFilter(){
//        return new TokenFilter();
//    }


    // // @Bean
    // public RequestTimeGatewayFilterFactory elapsedGatewayFilterFactory() {
    //     return new RequestTimeGatewayFilterFactory();
    // }


}
