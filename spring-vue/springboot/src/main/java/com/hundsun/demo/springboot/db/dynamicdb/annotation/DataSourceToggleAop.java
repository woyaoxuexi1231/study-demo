package com.hundsun.demo.springboot.db.dynamicdb.annotation;

import com.hundsun.demo.springboot.db.dynamicdb.core.DataSourceToggleUtil;
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
public class DataSourceToggleAop {

    @Pointcut(value = "@annotation(targetDataSource))")
    public void point(TargetDataSource targetDataSource) {
    }

    @Around(value = "point(targetDataSource)", argNames = "joinPoint,targetDataSource")
    public Object around(ProceedingJoinPoint joinPoint, TargetDataSource targetDataSource) throws Throwable {

        try {
            DataSourceToggleUtil.set(targetDataSource.value());
            return joinPoint.proceed();
        } finally {
            DataSourceToggleUtil.reSet();
        }
    }
}
