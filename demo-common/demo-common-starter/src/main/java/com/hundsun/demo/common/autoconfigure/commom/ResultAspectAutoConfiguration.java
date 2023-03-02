package com.hundsun.demo.common.autoconfigure.commom;

import com.hundsun.demo.commom.core.aop.ResultAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 16:22
 */
// @Configuration
// @EnableConfigurationProperties(ResultAspectConfiguration.class)
public class ResultAspectAutoConfiguration {

    // @Autowired
    ResultAspectConfiguration resultAspectConfiguration;

    // @Bean
    // @ConditionalOnMissingBean
    AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor(){
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(resultAspectConfiguration.getScanRange());
        advisor.setAdvice(new ResultAdvice());
        return advisor;
    }

}
