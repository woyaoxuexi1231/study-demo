package org.hulei.basic.jdk.java8;


import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    Java 8 发布的新特性
    lambda表达式是一个可传递的代码块,可以在之后一次或多次执行
     */

    public static void main(String[] args) {

        // 总的来说 lambda表达式就是 参数, 箭头(->), 以及一个代码块

        // 线程的创建使用 Lambda表达式之后
        Thread thread = new Thread(() -> System.out.println("lambda!"));
        // HashMap.foreach 操作也可使用 Lambda表达式
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "hello lambda!");
        map.forEach((k, v) -> System.out.println("key : " + k + " - value : " + v));

        // 函数式接口:对于只有一个抽象方法的接口,需要这种接口的对象时,就直接提供一个lambda表达式


        // 方法引用:指示编译器生成一个函数式接口的实例,覆盖这个接口的抽象方法来调用给定的方法
        // 这里会生成一个Consumer<? super T>的实例,这个实例的accept方法会调用System.out.println()方法来打印参数
        // **注意: 第一个参数会成为方法的隐式参数(如果有)  类似于 String::concat 等价于 (x,y) -> x.concat(y) 这里lambda表达式的第一个参数作为隐式参数,第二个作为显示参数
        Set<String> set = new HashSet<>();
        set.add("hello");
        set.add("world");
        set.forEach(System.out::println);


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
