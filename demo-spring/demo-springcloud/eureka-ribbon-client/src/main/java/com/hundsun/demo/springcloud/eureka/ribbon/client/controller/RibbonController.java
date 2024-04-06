package com.hundsun.demo.springcloud.eureka.ribbon.client.controller;

import com.hundsun.demo.springcloud.eureka.ribbon.client.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.ribbon.client.controller
 * @className: RibbonController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 23:09
 */

@RestController
public class RibbonController {

    @Autowired
    private RibbonService ribbonService;

    @GetMapping("/hi")
    public String hi() {
        return ribbonService.hi();
    }

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/testRibbon")
    public String testRibbon() {
        ServiceInstance choose = loadBalancerClient.choose("eureka-client");
        return choose.getHost() + ":" + choose.getPort();
    }
}
