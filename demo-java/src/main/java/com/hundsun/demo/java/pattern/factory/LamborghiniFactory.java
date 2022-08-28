package com.hundsun.demo.java.pattern.factory;

import com.hundsun.demo.java.pattern.Car;
import com.hundsun.demo.java.pattern.Lamborghini;

public class LamborghiniFactory {

    public static Car getCar() {
        return new Lamborghini();
    }
}
