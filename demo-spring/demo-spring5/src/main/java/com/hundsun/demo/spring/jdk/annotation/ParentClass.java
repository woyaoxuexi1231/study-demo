package com.hundsun.demo.spring.jdk.annotation;

import com.hundsun.demo.commom.core.annotation.DoneTime;

@SimpleAnnotation
public class ParentClass {
    @DoneTime
    public void someMethod() {
    }
}

class ChildClass extends ParentClass {
    @Override
    public void someMethod() {
        System.out.println("Child implementation");
    }
}