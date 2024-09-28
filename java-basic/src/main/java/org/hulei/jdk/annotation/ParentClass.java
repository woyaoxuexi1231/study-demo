package org.hulei.jdk.annotation;

import org.hulei.commom.core.annotation.DoneTime;

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