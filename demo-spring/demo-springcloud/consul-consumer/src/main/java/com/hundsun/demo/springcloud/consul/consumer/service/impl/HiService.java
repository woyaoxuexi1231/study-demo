package com.hundsun.demo.springcloud.consul.consumer.service.impl;

import com.hundsun.demo.springcloud.consul.consumer.feign.ConsumerClientFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.consul.consumer.service.impl
 * @className: HiService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 22:26
 */

@Service
public class HiService {

    @Autowired
    ConsumerClientFeign consumerClientFeign;

    public String sayHi() {
        return consumerClientFeign.sayHiFromConsulProvider();
    }
}
