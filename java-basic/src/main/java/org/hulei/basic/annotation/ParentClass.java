package org.hulei.basic.annotation;

@SimpleAnnotation
public class ParentClass {
    @SimpleAnnotation
    public void someMethod() {
    }
}

class ChildClass extends ParentClass {
    @Override
    public void someMethod() {
        System.out.println("Child implementation");
    }
}