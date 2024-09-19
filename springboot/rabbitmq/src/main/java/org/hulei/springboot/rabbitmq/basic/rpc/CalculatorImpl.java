package org.hulei.springboot.rabbitmq.basic.rpc;

// CalculatorImpl.java
public class CalculatorImpl implements Calculator {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
