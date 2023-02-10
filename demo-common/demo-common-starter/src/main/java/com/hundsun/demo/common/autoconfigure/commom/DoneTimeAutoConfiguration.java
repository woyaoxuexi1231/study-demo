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
 */

@Configuration
public class DoneTimeAutoConfiguration {

    @Bean
    public DoneTimeAspect doneTimeAspect() {
        return new DoneTimeAspect();
    }

}
