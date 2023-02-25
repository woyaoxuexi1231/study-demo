package com.hundsun.demo.springboot.aop;

import com.hundsun.demo.spring.jdbc.DynamicDataSourceTypeManager;
import com.hundsun.demo.springboot.annotation.TargetDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.aop
 * @className: DynamicDataSourceAspect
 * @description: 动态多数据源的切换切面
 * @author: h1123
 * @createDate: 2023/2/25 18:33
 */

@Aspect
@Order(-1)
public class DynamicDataSourceAspect {

    @Pointcut(value = "@annotation(targetDataSource))")
    public void point(TargetDataSource targetDataSource) {
    }

    @Around(value = "point(targetDataSource)")
    public Object around(ProceedingJoinPoint joinPoint, TargetDataSource targetDataSource) throws Throwable {

        try {
            DynamicDataSourceTypeManager.set(targetDataSource.dataSourceType());
            return joinPoint.proceed();
        } finally {
            DynamicDataSourceTypeManager.reSet();
        }
    }
}
