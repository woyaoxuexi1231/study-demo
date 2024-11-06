package org.hulei.springboot.spring.circle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassB {

    // @Autowired
    private ClassA a;

    @Autowired
    public void setA(ClassA a) {
        this.a = a;
    }

    public void methodB() {
        System.out.println(a);
    }
}