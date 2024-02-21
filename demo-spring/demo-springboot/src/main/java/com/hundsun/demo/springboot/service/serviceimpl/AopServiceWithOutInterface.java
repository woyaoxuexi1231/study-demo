package com.hundsun.demo.springboot.service.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service.serviceimpl
 * @Description:
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
