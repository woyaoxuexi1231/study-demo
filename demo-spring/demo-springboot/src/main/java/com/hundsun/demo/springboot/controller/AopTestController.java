package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.springboot.service.SimpleService;
import com.hundsun.demo.springboot.service.serviceimpl.AopServiceWithOutInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-02-21 15:20
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@RestController
@RequestMapping("/aop")
public class AopTestController {

    /**
     * author: hulei42031
     * date: 2024-02-21 15:21
     */
    @Autowired
    SimpleService simpleService;
    /**
     * 没有实现接口的一个bean
     * author: hulei42031
     * date: 2024-02-21 16:31
     */
    @Autowired
    AopServiceWithOutInterface aopServiceWithOutInterface;

    @GetMapping("/test")
    public void test() {
        // com.hundsun.demo.springboot.aop.SimpleAspect的切面控制
        // 这是一个在 com.hundsun.demo.springboot.service 有接口,并且实现这个接口的类的方法 - 这个会被切到
        simpleService.pageHelper();
        // 这是一个在 com.hundsun.demo.springboot.service 包下,没有实现接口的类的方法 -- 这个不会被切到
        aopServiceWithOutInterface.print();
    }
}
