package com.hundsun.demo.springboot.aop;

import com.hundsun.demo.springboot.aop.service.AopService;
import com.hundsun.demo.springboot.aop.service.impl.AopServiceImpl;
import com.hundsun.demo.springboot.aop.service.impl.AopServiceSubImpl;
import com.hundsun.demo.springboot.aop.service.impl.AopServiceWithOutInterface;
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
     * 有接口实现的bean
     */
    @Autowired
    AopServiceImpl aopServiceImpl;

    @Autowired
    AopServiceSubImpl aopServiceSubImpl;
    /**
     * 没有实现接口的一个bean
     */
    @Autowired
    AopServiceWithOutInterface aopServiceWithOutInterface;

    @GetMapping("/test")
    public void test() {
        // 代理的实现不会拘泥于是否有接口实现
        // aopServiceImpl.print(); // 有接口实现则使用Java自带的代理
        // aopServiceSubImpl.print(); // 有接口实现则使用Java自带的代理
        // aopServiceWithOutInterface.print(); // 没有实现接口的会使用cglib来代理
        System.out.println(aopServiceImpl.hello());
    }
}
