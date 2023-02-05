package com.hundsun.demo.java.pattern.creational;

import com.hundsun.demo.java.pattern.creational.builder.BenzAndFerrariBuilder;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create
 * @className: BuilderTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 18:33
 */

public class BuilderTest {

    /*
    建造者模式 - 将一个复杂对象的构建和它的表示分离, 使得同样的构建过程可以创建不同的表示
    客户端无需知道复杂对象的内部组成部分与装配方式, 只需要知道所需建造者的类型即可
     */

    public static void main(String[] args) {

        Car car = new BenzAndFerrariBuilder().createCar();
        System.out.println(car);
    }
}
