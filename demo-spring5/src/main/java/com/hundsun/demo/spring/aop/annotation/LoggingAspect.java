package com.hundsun.demo.spring.aop.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

// @EnableAspectJAutoProxy
@Aspect
public class LoggingAspect {
    @Before("execution(* com.hundsun.demo.spring.aop.annotation.MyService.*(..))")
    public void beforeAdvice() {
        System.out.println("Before method execution");
    }
}