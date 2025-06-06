package com.hundsun.demo.springcloud.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2023/5/8 21:18
 */

/*
@RefreshScope 是 Spring Cloud 提供的一个注解，它的作用是让被注解的 Bean 在配置变更后能够重新加载（刷新），从而实现配置的动态刷新。
被 @RefreshScope 修饰的类或 Bean 会在调用 /actuator/refresh 接口后重新创建，重新读取配置。

POST http://localhost:10013/actuator/refresh
 */
@RefreshScope
@RestController
public class HiController {

    @Value("${foo}")
    String fooBar;

    @GetMapping("/foo")
    public String getFooBar() {
        return fooBar;
    }

    @Value("${refresh}")
    String refresh;

    @GetMapping("/refresh")
    public String getRefresh() {
        return refresh;
    }
}
