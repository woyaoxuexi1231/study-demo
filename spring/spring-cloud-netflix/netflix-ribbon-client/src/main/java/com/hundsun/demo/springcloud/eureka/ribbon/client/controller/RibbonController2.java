package com.hundsun.demo.springcloud.eureka.ribbon.client.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/6/4 19:28
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class RibbonController2 {

    /**
     * 负载均衡器的核心组件, 可以获取负载均衡的服务提供者的实例信息
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @HystrixCommand(
            // commandKey = "myCommandKey",
            // groupKey = "myGroupKey",
            // threadPoolKey = ""
    )
    @GetMapping("/testRibbon")
    public String testRibbon() {
        log.info("testRibbon");
        ServiceInstance choose = loadBalancerClient.choose("eureka-client");
        return choose.getHost() + ":" + choose.getPort();
    }
}
