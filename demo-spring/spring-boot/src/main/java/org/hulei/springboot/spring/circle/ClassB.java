package org.hulei.springboot.spring.circle;

import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassB {

    private ClassA classA;

    @Autowired
    public void setA(ClassA a) {
        this.classA = a;
    }

    @DoneTime
    public void methodB() {
        System.out.println(classA);
    }
}