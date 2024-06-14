package com.hundsun.demo.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.dubbodemo.common.aop
 * @className: DoneTimeAspect
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 16:44
 */

@Aspect
@Component
@Slf4j
public class SimpleAspect {

    /**
     * com.hundsun.demo.springboot.service.*.*(..)这个范围并不能匹配到子包.
     * com.hundsun.demo.springboot.service..*.*(..)这个可以覆盖包含子包在内的所有类的方法
     * author: hulei42031
     * date: 2024-02-21 16:47
     */
    // @Pointcut(value = "execution(* com.hundsun.demo.springboot.aop.service.*.*(..)) && !execution(* com.hundsun.demo.springboot.aop.AopTestController.*(..))")
    @Pointcut(value = "execution(* com.hundsun.demo.springboot.aop.service.*.*(..)) || execution(* com.hundsun.demo.springboot.aop.service.impl.AopServiceWithOutInterface.*(..))")
    public void point() {
    }

    @Before(value = "point()")
    public void before(JoinPoint joinPoint) {
        // 进入方法之前调用, 这个在各个通知中是最先执行的
        System.out.println("this is before aop");
    }

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("this is around aop");
        return joinPoint.proceed();
    }

    @After(value = "point()")
    public void after(JoinPoint joinPoint) {
        System.out.println("this is after aop");
    }

    @AfterReturning(value = "point()", returning = "object")
    public void afterReturning(Object object) {
        System.out.printf("this is afterReturning aop, return: %s%n", object);
    }

    @AfterThrowing(value = "point()", throwing = "exception")
    public void afterThrowing(Exception exception) {
        System.out.printf("this is afterThrowing aop, exception: %s%n", exception.getMessage());
    }
}
