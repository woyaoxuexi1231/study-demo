package com.hundsun.demo.springcloud.consul.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.client.controller
 * @className: HiController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 22:53
 */

// 用于标识一个Bean在配置发生变化时需要被动态刷新。
@RefreshScope
@Slf4j
@RestController
public class HiController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hi")
    public String hi(HttpServletRequest req, HttpServletResponse rsp) {
        String s = "here is " + port + " and your ip is " + req.getRemoteAddr();
        log.info("{}", s);
        return s;
    }

    @Value("${foo:null}")
    String foo;

    @Value("${common:null}")
    String common;

    @GetMapping("/foo")
    public String getFoo() {
        log.info("foo: {}", foo);
        log.info("common: {}", common);
        return foo;
    }
}
