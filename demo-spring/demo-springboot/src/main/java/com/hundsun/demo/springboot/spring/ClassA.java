package com.hundsun.demo.springboot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassA {

    // @Autowired
    private ClassB b;

    @Autowired
    public void setB(ClassB b) {
        this.b = b;
    }

    public void methodA() {
        System.out.println(b);
    }
}