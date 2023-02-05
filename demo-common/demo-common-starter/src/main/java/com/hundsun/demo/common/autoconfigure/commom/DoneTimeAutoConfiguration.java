package com.hundsun.demo.common.autoconfigure.commom;


import com.hundsun.demo.dubbo.common.api.aop.DoneTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-05-23 17:41
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Configuration
public class DoneTimeAutoConfiguration {

    @Bean
    public DoneTimeAspect doneTimeAspect() {
        return new DoneTimeAspect();
    }

}
