package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.springboot.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:30
 */
@RestController("/simple")
public class SimpleController {

    /**
     *
     */
    @Autowired
    SimpleService simpleService;

    @GetMapping("/springTransaction")
    public void springTransaction() {
        simpleService.springTransaction();
    }

    @GetMapping("/SpringRedis")
    public void SpringRedis() {
        simpleService.springRedis();
    }
}
