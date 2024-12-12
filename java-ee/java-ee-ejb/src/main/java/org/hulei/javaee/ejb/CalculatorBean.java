package org.hulei.javaee.ejb;

import jakarta.ejb.Stateless;

@Stateless
public class CalculatorBean implements CalculatorLocal {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
