package com.hundsun.demo.java.jdk.pattern.creational;

import com.hundsun.demo.java.jdk.pattern.creational.factory.abs.BenzCarAbsFactory;
import com.hundsun.demo.java.jdk.pattern.creational.factory.factory.LamborghiniFactory;
import com.hundsun.demo.java.jdk.pattern.creational.factory.simple.StaticCarFactory;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.factory
 * @className: FactoryTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 17:42
 */

public class FactoryTest {

    public static void main(String[] args) {

        /*
        简单工厂模式 - 根据不同参数返回不同的对象
        不符合开闭原则, 一旦发生扩展必定会修改原始的代码, 没有任何抽象的思想
         */
        Car ferrari = StaticCarFactory.getCar("Ferrari");

        /*
        工厂模式 - 为每种不同的对象创建一个不同的工厂,隐藏创建对象的细节,通过工厂直接得到对象
        符合开闭原则
         */
        Car lamborghini = LamborghiniFactory.getCar();

        /*
        抽象工厂模式 - 当需要生产的产品不是一个简单的对象的时候, 比如一个完整的产品内需要生产不同的子产品才能生成一个完整的产品, 那么可以使用到抽象工厂模式
        当一个组装成一个产品的子产品的数量没有变化时, 这种设计模式是符合开闭原则的
        但是当组装成一个产品的子产品的数量发生变化时, 这种设计模式时不符合开闭原则的, 这会导致原有的工厂类发生改变
         */
        Car benz = BenzCarAbsFactory.getCar();

        /*
        类似于像Boolean.valueOf这样的方法同样也是工厂模式的实现, 它通过静态工厂方法返回 Boolean 类的实例对象
        工厂模式是一种创建型设计模式，其主要目的是封装对象的创建过程，并提供一个统一的接口来创建对象，从而隐藏对象的具体实现细节。
         */
    }
}
