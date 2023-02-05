package com.hundsun.demo.java.jdk;


import java.util.HashMap;
import java.util.Map;

/**
 * @ProductName: Java
 * @Package: com.hundsun.demo.java.jdk
 * @Description: Lambda表达式
 * @Author: HL
 * @Date: 2021/10/12 16:13
 * @Version: 1.0
 * <p>
 */

public class Lambda {


    /*
    Lambda表达式本质就是一个语法糖, 为了让语法更简单
    Java 8子发布的新特性
     */

    public static void main(String[] args) {

        // 线程的创建使用 Lambda表达式之后
        Thread thread = new Thread(() -> System.out.println("lambda!"));

        // HashMap.foreach 操作也可使用 Lambda表达式
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "hello lambda!");
        map.forEach((k, v) -> System.out.println("key : " + k + " - value : " + v));
    }

}
