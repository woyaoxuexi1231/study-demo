package org.hulei.jdk.jdk;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.java.reflect
 * @Description: Java反射机制
 * @Author: hulei42031
 * @Date: 2023-01-30 10:15
 */

public class Reflection {


    /*
    Java反射机制 - 在程序运行时再动态加载类并获取类的详细信息, 从而操作类或对象的属性和方法
    本质是 JVM 得到了 class 对象之后, 再通过 class 对象进行反编译, 从而获取对象的各种信息
    为什么要用到反射呢??
        1.需要的信息在运行时再动态的获取,而不是在编码阶段就确定,比如我们可能从文件中获取一串序列化字符串(json),恢复成对象就需要使用反射
        2.spring中扫描注解生成代理对象也是需要反射技术的,通过反射来获取对象的行为,mybatis框架也是一样的
     */

    public static void main(String[] args) {

        String className = "com.hundsun.demo.java.jdk.Reflection$Book";
        try {

            // 用Class类可以在JVM运行时获取类或接口的信息
            Class<?> clazz = Class.forName(className);

            // 使用newInstance()相当于直接调用无参构造方法创建对象, 如果对象没有无参构造方法会报错
            Object object = clazz.newInstance();
            System.out.println(object);
            // 也可以使用Constructor来指定参数构造对象
            Constructor<?> constructor = clazz.getConstructor(String.class);
            object = constructor.newInstance("Spring 5.0");
            System.out.println(object);

            // 获取对象的方法和属性
            Method[] methods = clazz.getMethods();
            Field[] fields = clazz.getFields();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 一个简单的Book类
     */
    @Data
    static class Book {

        /**
         * 书名
         */
        private String title;

        /**
         * 作者
         */
        private String author;

        /**
         * 出版日期
         */
        private Integer publicationDate;

        public Book() {
        }

        public Book(String title) {
            this.title = title;
        }
    }
}
