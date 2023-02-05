package com.hundsun.demo.java.pattern.creational;

import com.hundsun.demo.java.pattern.creational.singleton.SingletonCar;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create
 * @className: SingletonTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:21
 */

public class SingletonTest {

    /*
    单例模式 - 确保一个类只有一个实例, 并提供一个全局访问点来访问这个唯一实例

    Spring中的一个核心设计模式
     */

    public static void main(String[] args) {

        SingletonCar one = SingletonCar.getInstance();
        SingletonCar two = SingletonCar.getInstance();

        // is always true
        System.out.println(one == two);
    }
}
