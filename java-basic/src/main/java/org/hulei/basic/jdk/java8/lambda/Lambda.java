package org.hulei.basic.jdk.java8.lambda;


import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.sql.Time;
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

    public static void main(String[] args) {

        /*
        Lambda(λ) 表达式本质就是一个语法糖，为了让语法更简单。引入 lambda 表达式主要是为了两点：
            1. 一个可传递的代码块，可以在之后一次或多次执行
            2. 特别重要的一点就是，这个代码块在将来的某个时间被调用，也就是 延迟执行

        总的来说 lambda 表达式就是 参数, 箭头(->), 以及一个代码块。这其中带有两个特性：
            1. 如果可以推导出一个 lambda 表达式的参数类型，那么可以忽略其类型。
            2. 无需指定 lambda 表达式的返回类型，lambda 表达式的返回类型总会由上下文推导得出。
         */
        Thread thread = new Thread(() -> System.out.println("lambda!"));
        // HashMap.foreach 操作也可使用 Lambda表达式
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "hello lambda!");
        map.forEach((k, v) -> System.out.println("key : " + k + " - value : " + v));


        /*
        在引入 lambda 表达式之后，顺带也推出了函数式接口的概念。
        函数式接口：对于只有一个抽象方法的接口，需要这种接口时的对象时，可以提供一个 lambda 表达式。
        这也就意味着 lambda 表达式可以传递到函数式接口。

        Runnable 就是一个函数式接口，它被 @FunctionalInterface 注解修饰。
         */
        Runnable r1 = () -> System.out.println("lambda!");


        /*
        有时，lambda 表达式涉及一个方法。比如希望出现一个定时器就打印这个事件对象。
        这里的 System.out::println 就表示一个方法引用。它指示编译器生成一个函数式接口的实例，覆盖这个接口的抽象方法来调用给定的方法。

        * 类似于 lambda 表达式，方法引用也不是一个对象。不过，为一个类型为函数式接口的变量赋值的时候会产生一个对象。
        * 类似于 lambda 表达式，方法引用也不会独立存在，总会转换为函数式接口的实例。

        * 关于方法引用与等价的 lambda 表达式这里不列举。
         */
        var timer = new Timer(1000, System.out::println);


        /*
        关于 lambda 表达式的变量作用域
        1. 为了确保所捕获的值式明确定义的，在 lambda 表达式中，只能引用值不会改变的变量。
        2. lambda 表达式中捕获的变量必须是事实最终变量(effectively final)，即这个变量初始化后就不会再为它赋新值。
         */
        int i = 1;
        Runnable r2 = () -> System.out.println("lambda! " + i);
        // i = 2; // 如果这里注释放开，那么上面的 r2 会报错。 Variable used in lambda expression should be final or effectively final

        /*
        内部类 - 定义在另一个类中的类。需要内部类的原因主要两点
            1. 内部类可以对同一个包中的其他类隐藏。
            2. 内部类方法可以访问定义这些方法的作用域中的数据，包括私有的数据。

        内部类的应用有很多，在并发中亦有用到。

        内部类还延伸出 匿名内部类、静态内部类
         */
        SimpleClass simpleClass = new SimpleClass(1);
        log.info("{}", simpleClass.getInnerClass().getId());
    }

}


class SimpleClass {
    Integer id;

    public SimpleClass(Integer id) {
        this.id = id;
    }

    class InnerClass {
        public Integer getId() {
            // OutClass.this 表示外部类的引用
            return SimpleClass.this.id;
        }
    }

    public InnerClass getInnerClass() {
        return new InnerClass();
    }
}
