package com.hundsun.demo.springcloud.eureka.feign.client.service;

import com.hundsun.demo.springcloud.eureka.feign.client.config.FeignConfig;
import com.hundsun.demo.springcloud.eureka.feign.client.service.impl.HiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.service
 * @className: EurekaClientFeign
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 13:17
 */

@FeignClient(value = "eureka-client", configuration = FeignConfig.class, fallback = HiHystrix.class)
public interface EurekaClientFeign {

    @GetMapping("/hi")
    String sayHiFromClientEureka();
}
