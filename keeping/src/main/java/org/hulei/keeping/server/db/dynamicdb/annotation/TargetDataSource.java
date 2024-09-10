package org.hulei.keeping.server.db.dynamicdb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切换目标数据源的注解实现
 *
 * @author h1123
 * @since 2023/2/25 18:36
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TargetDataSource {

    String value() default "first";
}
