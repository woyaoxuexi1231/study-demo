package com.hundsun.demo.java.jdk.generic;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk
 * @className: Generic
 * @description:
 * @author: hl
 * @createDate: 2023/5/20 11:41
 */

public class GenericMain {

    public static void main(String[] args) {

        Generic<GenericImpl> generic = new Generic<>(new GenericImpl());
        // Type parameter 'java.lang.Object' is not within its bound; should implement 'com.hundsun.demo.java.jdk.generic.GenericInterface'
        // Generic<Object> objectGeneric = new Generic<>();

        generic.print();
        generic.getClass().getTypeParameters();

        GenericObject genericObject = new GenericObject(new GenericImpl());
        genericObject.print();
    }

    static class GenericImpl implements GenericInterface {

        @Override
        public void print() {
            System.out.println("hello");
        }
    }

    public static <T> void hhh(T t) {
        // nothing
        // 按照惯例，在方法声明中，先写访问修饰符（如 public），然后写其他修饰符（如 static），再写泛型声明（如 <T>），最后写返回类型（如 void）。
        // 当然，这只是约定俗成的写法，并不是强制要求。- 说是约定成俗的规矩 其实不是, 调换泛型声明的顺序编译器就不再识别了, 我觉得 gpt 回答得有问题
    }
}
