package com.hundsun.demo.dubbo.provider.filter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.dubbo.provider.filter
 * @className: MethodInvokeAspect
 * @description:
 * @author: h1123
 * @createDate: 2022/11/5 20:20
 * @updateUser: h1123
 * @updateDate: 2022/11/5 20:20
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@Aspect
@Component
@Slf4j
public class MethodInvokeAspect {

    @Pointcut(value = "execution(* com.hundsun.demo.dubbo.provider.api.service.*.*(..))")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void around(JoinPoint joinPoint) {

        // 方法参数
        Object[] param = joinPoint.getArgs();
        log.info("Invoke Method {}, Param: {}", joinPoint.getSignature(), param);
    }
}
