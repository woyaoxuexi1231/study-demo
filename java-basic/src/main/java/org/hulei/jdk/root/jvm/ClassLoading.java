package org.hulei.jdk.root.jvm;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.jvm
 * @className: ClassLoading
 * @description:
 * @author: h1123
 * @createDate: 2024/1/7 16:28
 */

public class ClassLoading {

    public static void main(String[] args) {
        /*
        1. 对于静态字段, 只有定义此字段的类才会被初始化, 所以这里只出发了其父类的初始化, 而没有出发其子类的初始化
        2. 通过数据来引用类, 也不会出发此类的初始化操作
        3. 常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化。
         */
        System.out.println(SubClass.value);
        /*
        结果如下:
        SuperClass init!
        123
         */
    }
}

class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;
}

class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init!");
    }
}

