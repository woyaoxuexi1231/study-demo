package com.hundsun.demo.spring.jdk.pattern.creational.singleton;

import com.hundsun.demo.spring.jdk.pattern.creational.Car;

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
        数据不一致性：lambda 表达式持有的堆副本和栈中的原始变量可能会出现数据不一致，因为它们不再指向相同的数据。 当我们指定一个lambda表达式的时候希望的是得到一个确切的执行计划, 而不是在确切的执行计划里还有变量存在
        线程安全问题：当 lambda 表达式在多线程环境中运行时，如果局部变量可以被更改，就会引入线程安全问题，因为不同的线程可能会试图同时读取和修改这个变量，导致不可预知的行为。
     */

    /**
     * 线程安全的单例模式包括
     * 1. 使用枚举实现, 枚举在 Java 中天生就是线程安全的，且只会加载一次,枚举类型是通过 enum 关键字进行声明的，它们在类加载的时候就被实例化，而且确保整个 JVM 中只有一个实例
     * 2. 使用静态内部类实现单例模式: 通过静态内部类的方式实现单例模式，当第一次加载 Singleton 类时不会初始化 instance，只有在第一次调用 Singleton.getInstance() 方法时才会导致 instance 被初始化
     * 3. 懒汉式+双重检查锁定: 在第一次调用获取单例实例的方法时才会进行实例化
     * 4. 饿汉式: 这种方式在类加载时就实例化单例对象，保证了线程安全，但可能会造成资源浪费
     * <p>
     * <p>
     * <p>
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

/**
 * 懒汉式+双重检查锁定
 */
class Singleton {

    /**
     * 防止指令重排序,需要使用volatile来保护代码不被重排序
     */
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

/**
 * 饿汉式（Eager Initialization）
 */
class Singleton2 {

    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        return instance;
    }
}

enum Singleton3 {
    INSTANCE;

    public void showMessage() {
        System.out.println("Hello from Singleton!");
    }
}
