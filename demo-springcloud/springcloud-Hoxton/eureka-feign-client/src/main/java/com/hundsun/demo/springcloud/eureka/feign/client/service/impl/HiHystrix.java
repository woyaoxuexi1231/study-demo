package com.hundsun.demo.springcloud.eureka.feign.client.service.impl;

import com.hundsun.demo.springcloud.eureka.feign.client.service.EurekaClientFeign;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.service.impl
 * @className: HiHystrix
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 14:43
 */

@Component
public class HiHystrix implements EurekaClientFeign {

    @Override
    public String sayHiFromClientEureka() {
        return "hi, sorry, error!";
    }

    @Override
    public String sayHi2FromClientEureka(String req, String req2) {
        return null;
    }
}
