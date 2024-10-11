package org.hulei.common.core.aop;

import cn.hutool.core.date.StopWatch;
import org.hulei.common.core.annotation.DoneTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.text.DecimalFormat;

/**
 * @projectName: study-demo
 * @package: com.hundsun.dubbodemo.common.aop
 * @className: DoneTimeAspect
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 16:44
 */

@Aspect
// @Component
@Slf4j
public class DoneTimeAspect {

    @Pointcut("execution(* com.hundsun.demo.*.*(..))")
    public void point() {

    }

    @Pointcut("@annotation(doneTime)")
    public void point2(DoneTime doneTime) {

    }

    @Around(value = "point2(doneTime)", argNames = "joinPoint,doneTime")
    public Object around(ProceedingJoinPoint joinPoint, DoneTime doneTime) throws Throwable {

        // 计时器
        StopWatch stopWatch = new StopWatch();
        // 方法参数
        Object[] param = joinPoint.getArgs();

        try {
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("time: {} ms, method: {}, param: {} ", new DecimalFormat("#,###").format(stopWatch.getLastTaskTimeNanos() / 1000 / 1000), joinPoint.getSignature().getName(), param);
        }

    }
}
