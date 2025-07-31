package org.hulei.basic.jdk;

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


    Method.invoke 的底层实现原理是什么？
      1. 获取目标对象和参数：invoke(Object obj, Object... args)，第一个参数是目标对象（静态方法则忽略），后面是实参。
      2. 检查访问权限：如果是私有方法，且没有 setAccessible(true)，则 JVM 会抛出 IllegalAccessException。
      3. 参数类型匹配与装箱/拆箱：反射会把 Object 数组的参数转换为实际参数类型（自动装箱/拆箱）。
      4. 本地方法调用：Method.invoke 最终会通过 JVM 的本地方法（JNI/C++）调用真实的目标方法。
            在 HotSpot JVM 中，Method.invoke 底层是由 MethodAccessor 实现的，实际调用会委托给 NativeMethodAccessorImpl 或 GeneratedMethodAccessorImpl：
             - NativeMethodAccessorImpl：最初是用 native 方式调用（JNI），性能较低。
             - GeneratedMethodAccessorImpl：JVM 会动态生成字节码来避免 JNI 的开销（称为“反射调用加速器”）。
    反射与动态代理有什么关系？
      反射是动态代理的基础。
       - Java 的动态代理（java.lang.reflect.Proxy）依赖反射 API 创建代理类。
       - 当你调用 Proxy.newProxyInstance 时，JVM 会：
         动态生成一个实现了你指定接口的新类（代理类）。
         代理类的方法实现内部是调用 InvocationHandler.invoke。
         InvocationHandler.invoke 中通常会通过 Method.invoke 来反射调用目标对象的实际方法（或者做其他逻辑，如拦截、增强）。
       - 因此，动态代理就是用反射在运行时生成和调度的“可插拔逻辑层”。

    Java 9+对反射机制做了哪些优化？ TODO
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

            for (Method method : methods) {
                method.invoke(object);
            }

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
