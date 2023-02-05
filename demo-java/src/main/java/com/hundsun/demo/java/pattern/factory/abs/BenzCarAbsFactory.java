package com.hundsun.demo.java.pattern.factory.abs;

import com.hundsun.demo.java.pattern.factory.Benz;
import com.hundsun.demo.java.pattern.factory.Car;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory.abs
 * @className: AbsCarFactory
 * @description: 抽象工厂模式
 * @author: h1123
 * @createDate: 2023/2/5 17:56
 */

public class BenzCarAbsFactory {

    /**
     * 返回特定的 Car对象
     *
     * @return car
     */
    public static Car getCar() {
        // 假设这里还需要进行 wheel 和 engine的组装才能返回这个Car, 而这两个组件又需要不同的工厂来返回
        return new Benz();
    }
}
