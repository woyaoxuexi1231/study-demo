package org.hulei.demo.cj.ratelimit.redis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RedisLimitAnnotation {

    /**
     * key
     */
    String key() default "";

    /**
     * Key的前缀
     */
    String prefix() default "";

    /**
     * 限流时间内限流次数
     */
    int count();

    /**
     * 限流时间，单位秒
     */
    int period();

    /**
     * 限流的类型(接口、请求ip、用户自定义key)
     */
    LimitTypeEnum limitType() default LimitTypeEnum.INTERFACE;

}
