package org.hulei.demo.cj.ratelimit.guava;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface GuavaLimitRateAnnotation {

    // 限制类型
    String limitType();
    
    // 每秒 5 个请求
    double limitCount() default 5d;
}
