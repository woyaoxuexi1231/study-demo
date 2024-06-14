package com.hundsun.demo.springboot.aop.service.impl;

import com.hundsun.demo.springboot.aop.service.AopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.aop
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-13 19:13
 */

@Service
@Slf4j
public class AopServiceImpl implements AopService {

    @Override
    public void print() {
        log.info("AopServiceImpl print");
    }

    @Override
    public String hello() {
        return "hello aop";
        // throw new RuntimeException("error");
    }
}
