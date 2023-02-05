package com.hundsun.demo.java.pattern.factory.factory;

import com.hundsun.demo.java.pattern.factory.Car;
import com.hundsun.demo.java.pattern.factory.Lamborghini;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.factory
 * @className: LamborghiniFactory
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:47
 */

public class LamborghiniFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        return new Lamborghini();
    }
}
