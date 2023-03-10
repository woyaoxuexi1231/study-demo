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

    /*
    内部类 -
        成员内部类
        局部内部类 - 定义在一个方法或者一个作用域内的类, 和成员内部类基本相同
        匿名内部类 - 一个没有名字的类
        静态内部类 - 用 static 修饰的内部类, 也叫做嵌套内部类
    Q1: 内部类能做什么
        1. 成员内部类可以直接访问外部类中的所有成员变量和成员方法
    Q2: 为什么要使用内部类
        1. 封装性, 我们可以在 HashMap 源码中看到, 有一个 EntrySet 的 final 成员内部类
        2. 实现多继承, 同样的就 HashMap 而言, HashMap 继承自 AbstractMap, EntrySet 继承自 AbstractSet

    匿名内部类与 lambda
        匿名内部类可以写成 lambda 表达式的形式
        lambda 表达式可以捕捉 静态变量(被 static修饰的成员变量), 实例变量(成员变量), 本地变量(方法内定义的变量, 包括入参)
        Q: 为什么在 lambda 表达式里面的值必须被修饰成 final 类型
            1. 作用域问题 // todo
     */

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
