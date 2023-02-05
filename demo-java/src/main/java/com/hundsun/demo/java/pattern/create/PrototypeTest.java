package com.hundsun.demo.java.pattern.create;

import com.hundsun.demo.java.pattern.create.prototype.CloneableCar;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.pattern.create
 * @className: PrototypeTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 18:52
 */

public class PrototypeTest {

    /*
    原型模式 - 使用原型实例指定待创建对象的类型, 并且通过复制这个原型来创建对象
    其实就是克隆, 克隆又分为浅克隆和深克隆
    浅克隆 - 当原型对象被复制时只复制它本身和其中包含的值类型的成员变量, 而引用类型的成员变量并没有复制, 仅仅只是复制一份地址给克隆对象
    深克隆 - 无论原型对象的成员变量是值类型还是引用类型, 都将复制一份给克隆对象

    也就是说, 我的理解是, 如果一个对象的所有属性都是基础类型, 那么只是单纯的实现cloneable接口, 那么可以实现深克隆
    如果属性中有引用类型, 那么必须重写clone方法来达到深克隆
     */

    public static void main(String[] args) {

        // 这里由于 CloneableCar 的属性全是简单类型, 所以clone方法相当于深克隆
        CloneableCar car = new CloneableCar();
        CloneableCar cloneCar = car.clone();
        CloneableCar deepCloneCar = car.deepClone();

        System.out.println();
    }
}
