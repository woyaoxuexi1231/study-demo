package com.hundsun.demo.spring.jdk.pattern.creational.factory.factory;

import com.hundsun.demo.spring.jdk.pattern.creational.Benz;
import com.hundsun.demo.spring.jdk.pattern.creational.Car;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.factory
 * @className: BenzFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:47
 */

public class BenzFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        return new Benz();
    }
}
