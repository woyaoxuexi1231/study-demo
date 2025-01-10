package org.hulei.basic.annotation;

import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException {

        // 1. 注解在方法上的继承
        Method method = ChildClass.class.getMethod("someMethod");
        SimpleAnnotation doneTime = method.getAnnotation(SimpleAnnotation.class);
        System.out.println(doneTime == null ? "方法继承不生效" : "方法继承生效");

        // 2. 注解在类上的继承
        SimpleAnnotation simpleAnnotation = ChildClass.class.getAnnotation(SimpleAnnotation.class);
        System.out.println(simpleAnnotation == null ? "类继承不生效" : "类继承生效");

        // 方法继承不生效
        // 类继承生效
    }
}