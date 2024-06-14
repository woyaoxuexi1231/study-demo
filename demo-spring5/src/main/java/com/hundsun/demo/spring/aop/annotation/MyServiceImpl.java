package com.hundsun.demo.spring.aop.annotation;

public class MyServiceImpl implements MyService {
    @Override
    public void doSomething() {
        System.out.println("Doing something...");
    }
}