package org.hulei.basic;

import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java
 * @className: test
 * @description:
 * @author: h1123
 * @createDate: 2023/11/13 22:46
 */

public class ExtendsTest {

    @SneakyThrows
    public static void main(String[] args) {

        Child child = new Child();
        child.method();
        child.superMethod();

        MethodType methodType = MethodType.methodType(void.class);
        MethodHandle handle = MethodHandles.lookup().findVirtual(Grandparent.class, "method", methodType).bindTo(child);
        handle.invoke();
    }
}

class Grandparent {
    public void method() {
        System.out.println("Grandparent's method");
    }
}

class Parent extends Grandparent {
    @Override
    public void method() {
        System.out.println("Parent's method");
    }
}

class Child extends Parent {

    @Override
    public void method() {

        System.out.println("Child's method");
    }

    public void superMethod() {
        // 调用父类（Parent）中的重写方法
        super.method();
        // 这是 ChatGPT 给出的答案, 但是明显这个是不对的
        // super.super.method();
        // 调用父类的父类（Grandparent）的方法
        // super.method();
    }

}
