package com.hundsun.demo.java.pattern.factory;

import com.hundsun.demo.java.pattern.Car;
import com.hundsun.demo.java.pattern.Ferrari;

/**
 * 工厂模式
 * 为每种不同的对象创建一个不同的工厂,隐藏创建对象的细节,通过工厂直接得到对象
 */
public class FerrariFactory {

    public static Car getCar(){
        return new Ferrari();
    }
}
