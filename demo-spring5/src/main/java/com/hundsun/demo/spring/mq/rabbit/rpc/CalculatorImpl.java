package com.hundsun.demo.spring.mq.rabbit.rpc;

// CalculatorImpl.java
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
