package com.hundsun.demo.spring.aop.xml;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @Aspect
// @Component
public class LoggingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

    // @Before("execution(* com.example.*Service.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Before executing method: {}", methodName);
    }


}
