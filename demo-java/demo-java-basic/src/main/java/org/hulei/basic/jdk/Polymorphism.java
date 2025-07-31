package org.hulei.basic.jdk;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Java多态的学习
 *
 * @author hulei
 * @since 2024/9/14 14:39
 */

@SuppressWarnings("ClassEscapesDefinedScope")
@Data
public class Polymorphism {

    String value = "Polymorphism";

    /**
     * 接收一个 Interface 类型的参数
     * 这里涉及的东西:
     * 1. 对于Interface这个类型的方法调用,在编译期间并不能得知,因为传进来的到底是 ParentClass 或者是 SubClass,在编译期间并不能知道
     * 所以这里引入了后期绑定(也叫做动态绑定,运行时绑定)
     * 2. 向上转型,这里接收的是Interface或者是继承(实现)Interface的类,也就是说对象既可以作为其本身使用,也可以作为其基类使用,这种能力就是向上转型
     *
     * @param i interface
     */
    public static void print(PolymorphismInterface i) {
        i.print();
    }

    public static void main(String[] args) {
        /*
        Java 多态主要分为两种：编译时多态 和 运行时多态
        编译时多态可以直接认为是方法的重载
        运行时多态可以认为是方法的重写(这主要体现在继承)

        Java多态(Polymorphism)的底层实现主要依靠以下两个核心机制：
            1. 方法表(Method Table/VTable)
             - 每个类在JVM中都有一个方法表，存储该类及其父类的所有可访问方法的实际入口地址
             - 子类的方法表会继承父类的方法表，并覆盖重写的方法
             - 方法调用时，JVM通过对象实际类型的方法表来查找正确的方法实现
            2. 动态绑定(Dynamic Binding)
             - 在运行时根据对象的实际类型(而非引用类型)决定调用哪个方法
             - 与静态绑定(如private/final/static方法和字段访问)相对
             - 通过invokevirtual字节码指令实现

         非private、非static、非final的方法都是虚方法，支持多态
         这也意味着 private、static、final 方法都是不可继承的

         */
        PolymorphismParentClass parent = new PolymorphismParentClass();
        PolymorphismSubClass child = new PolymorphismSubClass();
        print(parent);
        print(child);

    }

    /**
     * 这是一个内部类
     */
    class interClass {
        public void print() {
            // 对于一个内部类,它可以随意的访问他的外部类的变量
            System.out.println(value);
        }

        public Polymorphism getOuterClass() {
            // 内部类可以通过这种形式返回外部类的引用
            return Polymorphism.this;
        }
    }
}


interface PolymorphismInterface {

    void print();

    /**
     * 现在可以使用 default 关键字在接口中实现一些默认的方法
     */
    String s = "a string from a interface";

    default void defaultMethod() {
        System.out.println("this is a default method from a interface and " + s);
    }
}

@Data
class PolymorphismParentClass implements PolymorphismInterface {

    String string = "parent";

    /**
     * 至于这种形式的继承,可以说成是运行时多态,在后期绑定的帮助下,在运行期间才确定具体调用的是哪个类的print方法
     * 这种也被称为方法的重写
     */
    @Override
    public void print() {
        System.out.println(string);
    }

    /**
     * 而对于这种形式的方法,叫设计时多态,这种形式的多态在编译期间就可以得到具体调用的是哪个方法,方法的重载
     *
     * @param string req
     */
    public void print(String string) {
        System.out.println(this.string + "-" + string);
    }

    /**
     * 对于一个static修饰的方法,是不能被继承的
     * 同时不仅仅是static修饰的方法,包括属性,代码,对象,都会在类加载的时候按照顺序依次初始化,并且仅仅会初始化一次
     */
    public static void staticMethod() {
    }

    /**
     * 对于一个final修饰的方法,同样是不能被继承的
     * 这样做的好处不仅可能保证别人不破坏这个方法的逻辑,也可以有效的关闭后期绑定(虽然对程序的整体性能几乎没有任何影响).所以最主要的还是关闭了动态绑定
     */
    public final void finalMethod() {
    }

    /**
     * private方法是不会被继承的,但是子类是可以有相同方法签名的方法的,但是为了避免混淆,不建议这么做
     */
    private void privateMethod() {
        System.out.println("privateMethod from ParentClass");
    }
}

@EqualsAndHashCode(callSuper = true)
@Data
class PolymorphismSubClass extends PolymorphismParentClass implements PolymorphismInterface {

    Integer integer = 1;

    public void print() {
        System.out.println(integer);
        super.print();
    }

    private void privateMethod() {
        System.out.println("privateMethod from SubClass");
    }

}
