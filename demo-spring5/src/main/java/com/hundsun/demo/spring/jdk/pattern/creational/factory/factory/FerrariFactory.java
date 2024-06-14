package com.hundsun.demo.spring.jdk.pattern.creational.factory.factory;

import com.hundsun.demo.spring.jdk.pattern.creational.Car;
import com.hundsun.demo.spring.jdk.pattern.creational.Ferrari;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.factory
 * @className: FerrariFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:41
 */

public class FerrariFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        return new Ferrari();
    }
}
