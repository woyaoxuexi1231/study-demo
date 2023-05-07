package com.hundsun.demo.springcloud.consul.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.consul.consumer.service
 * @className: ConsumerClientFeign
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 22:25
 */

@FeignClient(value = "consul-provider")
public interface ConsumerClientFeign {

    @GetMapping("/hi")
    String sayHiFromConsulProvider();
}
