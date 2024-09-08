package com.hundsun.demo.springboot.mq.rabbit.rpc;

// CalculatorImpl.java
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
