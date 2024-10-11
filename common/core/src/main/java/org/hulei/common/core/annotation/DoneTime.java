package org.hulei.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
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
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface DoneTime {
}
