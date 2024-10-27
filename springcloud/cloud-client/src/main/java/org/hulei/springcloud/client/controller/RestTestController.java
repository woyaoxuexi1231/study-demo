package org.hulei.springcloud.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.client.controller
 * @className: RestTestController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 22:21
 */

@RestController
public class RestTestController {

    @Value(value = "${server.port}")
    private String port;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/testRest")
    public String testRest() {
        return restTemplate.getForObject("https://www.baidu.com", String.class);
    }


    @GetMapping("/restTemplateGetForObject")
    public void restTemplateGetForObject() {
        String object = restTemplate.getForObject("http://localhost:" + port + "/hi", String.class);
        System.out.println(object);
    }
}
