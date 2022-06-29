package com.hundsun.demo.dubbo.common.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @projectName: study-demo
 * @package: com.hundsun.dubbodemo.common.annotation
 * @className: DoneTime
 * @description:
 * @author: h1123
 * @createDate: 2022/5/22 16:42
 * @updateUser: h1123
 * @updateDate: 2022/5/22 16:42
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoneTime {
}
