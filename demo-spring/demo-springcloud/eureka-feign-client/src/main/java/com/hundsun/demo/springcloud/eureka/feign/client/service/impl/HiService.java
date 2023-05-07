package com.hundsun.demo.springcloud.eureka.feign.client.service.impl;

import com.hundsun.demo.springcloud.eureka.feign.client.service.EurekaClientFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.feign.client.service.impl
 * @className: HiService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 13:21
 */

@Service
public class HiService {

    @Autowired
    EurekaClientFeign eurekaClientFeign;

    public String sayHi() {
        return eurekaClientFeign.sayHiFromClientEureka();
    }
}
