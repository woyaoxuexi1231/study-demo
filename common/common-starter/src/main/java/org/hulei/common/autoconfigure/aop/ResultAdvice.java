package org.hulei.common.autoconfigure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hulei.util.utils.ResultDTOBuild;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.aop
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-16 15:28
 */
@Slf4j
public class ResultAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultDTOBuild.resultErrorBuild("failed");
        }
    }
}
