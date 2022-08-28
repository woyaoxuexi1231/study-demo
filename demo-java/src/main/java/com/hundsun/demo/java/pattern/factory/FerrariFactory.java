package com.hundsun.demo.java.pattern.factory;

import com.hundsun.demo.java.pattern.Car;
import com.hundsun.demo.java.pattern.Ferrari;

public class FerrariFactory {

    public static Car getCar(){
        return new Ferrari();
    }
}
