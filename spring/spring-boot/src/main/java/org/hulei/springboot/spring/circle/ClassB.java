package org.hulei.springboot.spring.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassB {

    private ClassA classA;

    @Autowired
    public void setA(ClassA a) {
        this.classA = a;
    }

    public void methodB() {
        System.out.println(classA);
    }
}