package org.hulei.jdk.root.jvm;

/**
 * @author hulei
 * @since 2024/8/12 18:13
 * <p>
 * jvm内存管理
 */

public class JvmMemory {

    /*
    1.程序计数器(线程私有)-主要用来存储当前线程正在执行的虚拟机字节码指令的地址
    2.Java虚拟机栈(线程私有)-每个方法被执行的时候,Java虚拟机都会同步创建一个栈帧(Stack Frame)用于存储局部变量表、操作数栈、动态连接、方法出口等信息
    3.本地方法栈(线程私有)-与虚拟机栈所发挥的作用是非常相似的，其区别只是虚拟机栈为虚拟机执行Java方法（也就是字节码）服务，而本地方法栈则是为虚拟机使用到的本地(Native)方法服务。
    4.Java堆(线程共享)-此内存区域的唯一目的就是存放对象实例，Java世界里“几乎”所有的对象实例都在这里分配内存
    5.方法区(线程共享)-它用于存储已被虚拟机加载的类型信息、常量、静态变量、即时编译器编译后的代码缓存等数据
    6.运行时常量池(线程共享)-常量池表(Constant Pool Table)，用于存放编译期生成的各种字面量与符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。

    jdk1.8之后jvm内存区域内不再存在方法区和运行时常量池,这两个被移动到机器的直接内存中去了
     */

    // 常量将存储在方法区内(运行时常量池), 这里 'constant' 这个字面量以及这个变量的类型 java.lang.String 将以 Ljava/lang/String 这样的描述符存储在运行时常量池中
    private static final String constant = "Method Area";


    public static void main(String[] args) {
        // 虚拟机栈 - 执行methodA()前, 此时创建了新的栈帧,含局部变量表（存储args数组）和操作数栈
        methodA();
    }

    public static void methodA() {
        // 虚拟机栈 - 此时执行methodA, methodA被调用时, 会在main方法的栈帧之上创建一个新的栈帧，包含局部变量表（存储x和y）和操作数栈。
        //          局部变量表中存储了x和y的值，操作数栈用于临时存放计算结果。
        int x = 10;
        int y = 20;
        methodB(x, y);
    }

    public static void methodB(int a, int b) {
        // methodB被调用时，会在methodA的栈帧之上创建一个新的栈帧，包含局部变量表（存储a、b和sum）和操作数栈。
        int sum = a + b;
        System.out.println("The sum is: " + sum);
        methodC();
    }

    public static void methodC() {
        // 此对象将在堆上分配,但是栈帧内将保存一个引用类型的变量
        SimpleObject object = new SimpleObject();
        System.out.println(object);
        methodD();
    }

    public static void methodD() {
        System.out.println(constant);
    }

}
