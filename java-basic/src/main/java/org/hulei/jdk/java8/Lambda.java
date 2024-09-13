package org.hulei.jdk.java8;


import lombok.extern.slf4j.Slf4j;

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

@Slf4j
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

        // 内部类方法可以访问该类定义所在的作用域中的数据，包括私有的数据。
        SimpleClass simpleClass = new SimpleClass(1);
        SimpleClass.InnerClass innerClass = simpleClass.getInnerClass();
        log.info("{}", innerClass.getId());
    }

}


class SimpleClass {
    Integer id;

    public SimpleClass(Integer id) {
        this.id = id;
    }

    class InnerClass {
        public Integer getId() {
            return SimpleClass.this.id;
        }
    }

    public InnerClass getInnerClass() {
        return new InnerClass();
    }
}
