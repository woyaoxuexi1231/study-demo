package com.hundsun.demo.springboot.spring.transactional;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.spring.transactional
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-05-09 15:02
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@RestController
@RequestMapping("/transactional")
public class TransactionController {

    @Autowired
    TransactionTest transactionTest;

    @SneakyThrows
    @GetMapping("/transactionalLost")
    public void transactionalLost(String name) {
        transactionTest.transactionalLost(name);
    }

    @RequestMapping("/sqlSessionTransaction")
    public void sqlSessionTransaction() {
        transactionTest.sqlSessionTransaction();
    }

    @RequestMapping("/transactionalInherited")
    public void transactionalInherited() {
        transactionTest.transactionalInherited();
    }

    @RequestMapping(value = "/deadLock")
    public void deadLock() {
        transactionTest.deadLock();
    }
}
