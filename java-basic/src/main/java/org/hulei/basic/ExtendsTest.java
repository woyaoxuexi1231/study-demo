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

        // 这是new Bing 的答案
        /*
        在Java中，子类可以通过`super`关键字方便地调用父类中的方法，但是要调用祖父类的方法就比较复杂了。
        在拥有`invokedynamic`和`java.lang.invoke`包之前，使用纯Java语言很难处理这个问题，原因是子类的方法中根本没有办法获取一个实际类型是祖父类的对象引用⁵。
        然而，有了这两个包，Java程序员可以直接解决这个问题⁵。在JDK7 update9之前，可以使用以下方法解决⁵。具体的实现方式可能需要你查阅相关的Java文档或者参考一些专业的Java编程书籍。
        请注意，这种做法在实际编程中并不常见，因为它违反了面向对象编程的一些基本原则，如封装和继承。
        通常，如果你发现自己需要在子类中调用祖父类的方法，这可能意味着你的类层次结构需要重新设计。
        在大多数情况下，你应该通过重写方法或者使用接口来实现类之间的交互，而不是直接调用祖父类的方法。这样可以使你的代码更易于理解和维护。
        Source: Conversation with Bing, 2023/11/16
        (1) java如何通过子类调用祖父类的_java 如何跳过父类直接调用祖父类的方法-CSDN博客. https://blog.csdn.net/u013915451/article/details/107452113.
        (2) java父类获取子类对象、调用子类方法 - CSDN博客. https://blog.csdn.net/cpcpcp123/article/details/104016445.
        (3) java子类调用父类的方法_java 子类调用父类的方法-CSDN博客. https://blog.csdn.net/weixin_42346650/article/details/86091193.
        (4) Java如何在子类方法中调用父类被覆盖的实例方法？ -问答-阿里云开发者社区-阿里云. https://developer.aliyun.com/ask/282033.
        (5) 关于Java中子类调用父类方法 - 美好的明天 - 博客园. https://www.cnblogs.com/alsf/p/9286798.html.
         */
    }

}
