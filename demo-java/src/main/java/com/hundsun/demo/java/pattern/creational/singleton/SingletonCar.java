package com.hundsun.demo.java.pattern.creational.singleton;

import com.hundsun.demo.java.pattern.creational.Car;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create.singleton
 * @className: SingletonCar
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 19:10
 */

public class SingletonCar extends Car {

    /**
     * 使用静态内部类实现单例模式 - IoDH Initialization on Demand Holder
     * <p>
     * 静态内部类没有作为 SingletonCar 的成员变量内直接实例化, 因此类加载时不会实例化 SingletonCar
     * 第一次调用 getInstance时会加载内部类HolderClass, 在内部类定义了一个 static类型的变量 instance
     * 此时会首先初始化这个成员变量, 由 Java虚拟机来保证其线程安全, 确保成员变量只能初始化一次, 由于没有上锁, 性能没有任何影响
     * <p>
     * 除此之外还有饿汉式加载(类加载时就创建唯一实例)、懒汉式+双重检查锁定
     */
    private static class HolderClass {
        private final static SingletonCar instance = new SingletonCar();
    }

    public static SingletonCar getInstance() {
        return HolderClass.instance;
    }
}
