package org.hulei.common.autoconfigure.commom;

import org.hulei.commom.core.aop.ResultAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;

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
    AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(resultAspectConfiguration.getScanRange());
        advisor.setAdvice(new ResultAdvice());
        return advisor;
    }

}
