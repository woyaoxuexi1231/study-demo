package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.service.HiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-22 16:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Component
@Slf4j
public class HiServiceImpl implements HiService {

    @Override
    public void sayHi() {
        log.info("hi");
    }

    public void init(){
        log.info("HiServiceImpl initializing...");
    }

    public void destroy(){
        log.info("HiServiceImpl destroying...");
    }
}
