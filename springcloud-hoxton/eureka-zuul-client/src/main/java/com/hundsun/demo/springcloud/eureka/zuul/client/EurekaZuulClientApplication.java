package com.hundsun.demo.springcloud.eureka.zuul.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

@Deprecated
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class EurekaZuulClientApplication {

    public static void main(String[] args) {
        /*
        zuul.routes.hiapi.path=/hiapi/**
        zuul.routes.hiapi.service-id=eureka-client
        zuul.routes.ribbon.path=/ribbonapi/**
        zuul.routes.ribbon.service-id=eureka-ribbon-client
        zuul.routes.feign.path=/feignapi/**
        zuul.routes.feign.service-id=eureka-feign-client
        zuul.prefix=/v1
         */
        SpringApplication.run(EurekaZuulClientApplication.class, args);
    }
}
