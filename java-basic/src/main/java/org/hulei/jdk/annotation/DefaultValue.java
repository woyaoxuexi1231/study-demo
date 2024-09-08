package org.hulei.jdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * 对于注解的属性, value是一个默认属性值, 如果在使用注解时没有特别表明字段, 那么直接写入的值将赋予 value 字段
 *
 * @author hulei42031
 * @since 2024-05-21 14:49
 */

public class DefaultValue {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface DefaultValueAnno {

        String path() default "";

        String[] value() default {};

        String[] name() default {};
    }

    static class DefaultValueClass {

        @DefaultValueAnno("123")
        void print() {

        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(DefaultValueClass.class.getDeclaredMethod("print").getAnnotations()));
    }
}
