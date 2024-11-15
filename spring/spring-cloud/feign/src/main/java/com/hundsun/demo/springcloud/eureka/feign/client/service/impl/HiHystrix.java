package com.hundsun.demo.springcloud.eureka.feign.client.service.impl;

import com.hundsun.demo.springcloud.eureka.feign.client.service.CloudClientFeign;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2023/5/7 14:43
 */

@Component
public class HiHystrix implements CloudClientFeign {

    @Override
    public String sayHiFromClientEureka() {
        return "hi, sorry, error!";
    }

    @Override
    public String sayHi2FromClientEureka(String req, String other) {
        return "null";
    }
}
