package com.hundsun.demo.spring.jdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.annotation
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-05-21 14:49
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2024 Hundsun Technologies Inc. All Rights Reserved
 */

public class DefaultValue {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface DefaultValueAnno {

        String path() default "";

        // @AliasFor("name")
        String[] value() default {};

        // @AliasFor("value")
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
