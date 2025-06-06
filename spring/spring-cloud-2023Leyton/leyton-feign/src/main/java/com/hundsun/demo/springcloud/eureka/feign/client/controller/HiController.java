package com.hundsun.demo.springcloud.eureka.feign.client.controller;

import com.hundsun.demo.springcloud.eureka.feign.client.service.CloudClientFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hulei
 * @since 2023/5/7 13:22
 */

@RestController
public class HiController {

    @Resource
    CloudClientFeign cloudClientFeign;

    @GetMapping("/hi")
    public String hi() {
        return cloudClientFeign.sayHiFromClientEureka();
    }

    @GetMapping("/hi2")
    public String hi2() {
        return cloudClientFeign.sayHi2FromClientEureka("req", "req2");
    }
}
