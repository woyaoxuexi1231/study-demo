package com.hundsun.demo.springcloud.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.config.client
 * @className: HiController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/8 21:18
 */

@RestController
@RefreshScope
public class HiController {

    @Value("${foo}")
    String fooBar;

    @GetMapping("/foo")
    public String getFooBar() {
        return fooBar;
    }
}
