package org.hulei.springdata.routingdatasource.annotation;

import lombok.extern.slf4j.Slf4j;
import org.hulei.springdata.routingdatasource.core.DataSourceToggleUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.aop
 * @className: DynamicDataSourceAspect
 * @description: 动态多数据源的切换切面
 * @author: h1123
 * @createDate: 2023/2/25 18:33
 */

@Component
@Aspect
@Order(-1)
@Slf4j
public class DataSourceToggleAop {

    @Pointcut(value = "@annotation(targetDataSource))")
    public void point(TargetDataSource targetDataSource) {
    }

    @Around(value = "point(targetDataSource)", argNames = "joinPoint,targetDataSource")
    public Object around(ProceedingJoinPoint joinPoint, TargetDataSource targetDataSource) throws Throwable {

        try {
            DataSourceToggleUtil.set(targetDataSource.value());
            log.info("当前线程的数据库标记为: {}", targetDataSource.value());
            return joinPoint.proceed();
        } finally {
            DataSourceToggleUtil.reSet();
        }
    }
}
