package org.hulei.springboot.spring.aop;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.aop
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-13 18:00
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

// @Configuration
// @EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        AnnotationAwareAspectJAutoProxyCreator creator = new AnnotationAwareAspectJAutoProxyCreator();
        creator.setProxyTargetClass(true); // 使用CGLIB代理
        return creator;
    }

    // Bean definitions for PaymentService and LoggingAspect...
}
