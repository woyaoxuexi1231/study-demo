package com.hundsun.demo.springcloud.consul.provider.controller;

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

@RefreshScope
@RestController
public class HiController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hi")
    public String hi(HttpServletRequest req, HttpServletResponse rsp) {
        return "here is " + port + " and your ip is " + req.getRemoteAddr();
    }

    @Value("${foo.bar}")
    String fooBar;

    @GetMapping("/foo")
    public String getFooBar() {
        return fooBar;
    }
}
