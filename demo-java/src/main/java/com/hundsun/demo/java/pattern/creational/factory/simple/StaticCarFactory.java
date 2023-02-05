package com.hundsun.demo.java.pattern.creational.factory.simple;

import com.hundsun.demo.java.pattern.creational.Car;
import com.hundsun.demo.java.pattern.creational.Benz;
import com.hundsun.demo.java.pattern.creational.Ferrari;
import com.hundsun.demo.java.pattern.creational.Lamborghini;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.simple
 * @className: CarStaticFactory
 * @description: 静态工厂
 * @author: h1123
 * @createDate: 2023/2/5 17:39
 */

public class StaticCarFactory {

    /**
     * 根据不同的参数获取不同的 Car对象
     *
     * @param carName 汽车名字
     * @return 汽车
     */
    public static Car getCar(String carName) {

        switch (carName) {
            case "Ferrari":
                return new Ferrari();
            case "Lamborghini":
                return new Lamborghini();
            case "Benz":
                return new Benz();
            default:
                return new Car();
        }
    }
}
