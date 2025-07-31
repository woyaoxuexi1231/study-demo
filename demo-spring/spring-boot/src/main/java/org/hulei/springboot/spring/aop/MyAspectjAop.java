package org.hulei.springboot.spring.aop;

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
public class MyAspectjAop {

    /**
     * 匹配方法的执行: execution(方法访问修饰符 方法的全限定类名) *表示所有 ..表示要匹配子包以及子包下所有的 || 或者
     * 匹配特定类型的类中的方法: within
     * author: hulei42031
     * date: 2024-02-21 16:47
     */
    @Pointcut(value = "execution(* org.hulei.springboot.spring.aop.service.impl.AopServiceImpl.*(..)) || execution(* org.hulei.springboot.spring.aop.service.impl.AopServiceWithOutInterface.*(..))")
    public void point() {
    }

    @Before(value = "point()")
    public void before(JoinPoint joinPoint) {
        // 进入方法之前调用, 这个在各个通知中是最先执行的
        log.info("方法 {} 之前", joinPoint.getSignature().getName());
    }

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("方法 {} 之前(环绕)", joinPoint.getSignature().getName());
        try {
            return joinPoint.proceed();
        } finally {
            log.info("方法 {} 之后(坏绕)", joinPoint.getSignature().getName());
        }
    }

    @After(value = "point()")
    public void after(JoinPoint joinPoint) {
        log.info("方法 {} 之后", joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "point()", returning = "object")
    public void afterReturning(JoinPoint joinPoint, Object object) {
        log.info("方法 {} 返回之后, 结果: {}", joinPoint.getSignature().getName(), object);
    }

    @AfterThrowing(value = "point()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("方法 {} 抛出异常之后", joinPoint.getSignature().getName(), exception);
    }
}
