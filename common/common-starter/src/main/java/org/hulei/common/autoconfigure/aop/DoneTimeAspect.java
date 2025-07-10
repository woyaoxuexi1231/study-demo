package org.hulei.common.autoconfigure.aop;

import cn.hutool.core.date.StopWatch;
import org.hulei.common.autoconfigure.annotation.DoneTime;
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

        // 方法结果
        Object rsp = null;

        try {
            stopWatch.start();
            rsp = joinPoint.proceed();
            return rsp;
        } finally {
            stopWatch.stop();
            log.info("方法名：{}，耗时：{} 毫秒，入参：{}，结果：{}",
                    joinPoint.getSignature().getName(),
                    // 创建数字格式化对象，模式"#,###"表示：#：可选数字位（如果是0则不显示），,：千分位分隔符
                    new DecimalFormat("#,###").format(stopWatch.getLastTaskTimeNanos() / 1000 / 1000),
                    param,
                    rsp);
        }

    }
}
