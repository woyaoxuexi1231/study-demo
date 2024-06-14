package com.hundsun.demo.springboot.aop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service.serviceimpl
 * @Description: 这是一个没有实现任何任何接口的bean
 * @Author: hulei42031
 * @Date: 2023-09-15 13:24
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Slf4j
@Service
public class AopServiceWithOutInterface {

    public void print() {
        log.info("{}", "print");
    }
}
