package com.hundsun.demo.java.pattern.factory.factory;

import com.hundsun.demo.java.pattern.factory.Benz;
import com.hundsun.demo.java.pattern.factory.Car;

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
