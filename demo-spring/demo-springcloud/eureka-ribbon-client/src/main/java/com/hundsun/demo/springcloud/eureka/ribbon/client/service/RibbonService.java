package com.hundsun.demo.springcloud.eureka.ribbon.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.ribbon.client.service
 * @className: RibbonService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 23:04
 */

@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    public String hi() {
        return restTemplate.getForObject("http://eureka-client/hi", String.class);
    }
}
