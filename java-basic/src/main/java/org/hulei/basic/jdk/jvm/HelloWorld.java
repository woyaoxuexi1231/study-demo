package org.hulei.basic.jdk.jvm;

/**
 * @author hulei
 * @since 2024/8/12 17:47
 * <p>
 * 这是一个带package的类,那么在javac编译之后,需要在包结构的最外层调用java命令:
 * java com.hundsun.demo.spring.jdk.jvm.HelloWorld
 */

public class HelloWorld {

    /**
     * 在Java中，`main` 方法必须是静态的（`static`）的原因主要有两点：
     * <p>
     * 1. **程序的入口点**：<br>
     * - Java程序的执行从`main`方法开始。当JVM启动时，它会加载指定的类，并查找其中的`public static void main(String[] args)`
     * 方法作为程序的入口点。<br>
     * - 静态方法可以直接通过类名调用，而不需要先创建类的实例。这样做有助于简化启动过程，因为在执行`main`方法之前，JVM还没有创建任何实例对象。
     * <p>
     * 2. **避免实例化对象**：<br>
     * - `main` 方法被设计为静态的，可以直接通过类名调用，而无需先创建类的实例对象。这在程序入口的情况下是非常重要的，因为在程序启动时，尚未有任何对象实例化。<br>
     * - 如果`main`方法不是静态的，那么在调用`main`方法之前，JVM需要实例化类的对象，这将与程序的启动过程不符合。
     * <p>
     * 因此，为了确保Java程序的正确启动和执行流程，`main`方法必须声明为静态的，这样可以保证JVM能够直接调用它作为程序的入口点，而无需先创建类的实例对象。
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}
