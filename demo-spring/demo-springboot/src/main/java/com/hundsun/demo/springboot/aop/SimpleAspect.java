package com.hundsun.demo.springboot.aop;

import cn.hutool.core.date.StopWatch;
import com.hundsun.demo.commom.core.annotation.DoneTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

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

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时器
        StopWatch stopWatch = new StopWatch();
        // 方法参数
        Object[] param = joinPoint.getArgs();
        try {
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("SimpleAspect => Method {}, Param: {}, Time: {}ms", joinPoint.getSignature(), param, stopWatch.getTotalTimeMillis());
        }
    }
}
