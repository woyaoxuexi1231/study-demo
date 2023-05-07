package com.hundsun.demo.springcloud.eureka.feign.client.controller;

import com.hundsun.demo.springcloud.eureka.feign.client.service.impl.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.controller
 * @className: HiController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 13:22
 */

@RestController
public class HiController {

    @Autowired
    HiService hiService;

    @GetMapping("/hi")
    public String hi() {
        return hiService.sayHi();
    }
}
