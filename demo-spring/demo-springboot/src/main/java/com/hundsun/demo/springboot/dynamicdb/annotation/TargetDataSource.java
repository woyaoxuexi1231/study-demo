package com.hundsun.demo.springboot.dynamicdb.annotation;

import com.hundsun.demo.springboot.dynamicdb.core.DynamicDataSourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.annotation
 * @className: TargetDataSource
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 18:36
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TargetDataSource {

    DynamicDataSourceType dataSourceType() default DynamicDataSourceType.MASTER;
}
