package com.hundsun.demo.springcloud.gateway;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@RestController
@EnableEurekaClient
// @EnableDiscoveryClient
@Slf4j
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {

        /*
        DiscoveryClient_EUREKA-CLIENT/LAPTOP-HGITO649:eureka-client:9101 - registration status: 204
        注册成功
         */
        SpringApplication.run(GatewayApplication.class, args);
    }

    // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("add_request_parameter_route",
    //                     r -> r.path("/eureka-client/**")
    //                             .filters(f -> f.addRequestParameter("paramName", "paramValue"))
    //                             .uri("lb://eureka-client"))
    //             .build();
    // }

    @SentinelResource(value = "yourApi",
            blockHandler = "handleBlock")
    @GetMapping("/hi")
    public String hi() {
        return "hello world";
    }

    // 限流后的处理方法（参数、返回值需与原方法相同）
    public String handleBlock(BlockException ex) {
        return "Request has been blocked!";
    }

    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/test/{userinfo}")
    public Object test(@PathVariable String userinfo) {
        String url = "http://eureka-client/";
        return restTemplate.getForObject(url + "hi", Object.class);
    }
}
