package com.hundsun.demo.java.pattern.simplefac;

import com.hundsun.demo.java.pattern.Car;
import com.hundsun.demo.java.pattern.Ferrari;
import com.hundsun.demo.java.pattern.Lamborghini;


public class CarStaticFactory {

    /**
     * 静态工厂, 根据不同的参数获取不同的对象
     * 不符合开闭原则, 新增对象会入侵工厂类的代码
     *
     * @param carName
     * @return
     */
    public Car getCar(String carName) {
        switch (carName) {
            case "Ferrari":
                return new Ferrari();
            case "Lamborghini":
                return new Lamborghini();
            default:
                return new Car();
        }
    }
}
