package com.hundsun.demo.springcloud.eureka.client.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author woaixuexi
 * @since 2024/4/6 21:06
 */

@Slf4j
@Aspect
@Component
public class AopConfig {

    @Pointcut(value = "execution(* com.hundsun.demo.springcloud.eureka.client.controller.*.*(..))")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("method: {}", pjp.getSignature().getName());
        return pjp.proceed();
    }
}
