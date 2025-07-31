package org.hulei.basic.jdk.jvm;

import lombok.SneakyThrows;
import org.hulei.basic.jdk.io.NIOUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.jvm
 * @className: ClassLoad
 * @description:
 * @author: h1123
 * @createDate: 2023/10/27 23:34
 */

public class MyClassLoader {
    /*
    假设有如下的 Java 代码片段：
    ```java
    public class Main {
        public static void main(String[] args) {
            MyClass myObj = new MyClass();
            myObj.doSomething();
        }
    }
    class MyClass {
        public void doSomething() {
            System.out.println("Doing something!");
        }
    }
    ```
    在这个例子中，`Main` 类的 `main` 方法中创建了一个 `MyClass` 类的对象 `myObj`，并调用了它的 `doSomething` 方法。
    在 JVM 虚拟机的类加载过程中的解析阶段，会对这些符号引用进行解析操作。
    首先，解析阶段会对 `MyClass` 类进行符号引用验证，确保该类存在，并且具有公共可访问性。
    接下来，解析阶段会将 `MyClass` 类的符号引用解析为直接引用，即获取该类的内存地址或偏移量信息。
    在类型检查阶段，解析阶段会检查符号引用和目标的类型是否匹配，以确保调用方法的合法性。
    最后，解析阶段会将 `myObj.doSomething()` 中的符号引用替换为直接引用，以便在后续的字节码执行阶段，更高效地访问并执行 `doSomething` 方法。
    通过解析阶段，JVM 虚拟机能够对类和方法进行合法性检查，并建立符号引用与实际对象之间的关联，从而在程序运行时准确地找到并执行相应的代码。
     */

    @SneakyThrows
    public static void main(String[] args) {
        // 获取当前线程的默认的类加载器
        System.out.println(Thread.currentThread().getContextClassLoader());
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getContextClassLoader());
        });
        thread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        thread.start();

        File jarFile = new File(NIOUtil.getResourcePath("/custom-jdk-1.0-SNAPSHOT.jar"));
        System.out.println(jarFile.exists());
        URL jarURL = jarFile.toURI().toURL();

        CustomJarClassLoader customJarClassLoader = new CustomJarClassLoader(new URL[]{jarURL});
        // 这里会出现加载异常 Prohibited package name: java.util ,不允许我们包的名称出现这种类似的格式
        System.out.println(customJarClassLoader.loadClass("java.util.HashMap").getName());
    }
}

/**
 * 一个自定义的类加载器
 *
 * @author hulei
 * @since 2024/8/12 17:59
 */
class CustomJarClassLoader extends URLClassLoader {

    public CustomJarClassLoader(URL[] urls) {
        super(urls);
    }

    /**
     * 此方法在jdk1.2之前就存在,而双亲委派机制在1.2才开始出现
     * 为了兼容之前已经存在的用户自定义的类加载器,Java设计者做出妥协,声明了另一个方法 {@link URLClassLoader#findClass(String)},并且引导用户尽可能地重写这个方法来进行类加载的逻辑
     * 而loadClass这个方法则在 {@link java.lang.ClassLoader} 这个类中实现了双亲委派机制的具体逻辑
     * <p>
     * 所以总结: 1.此方法Java不推荐实现,他作为默认的双亲委派机制的实现 2.自定义的类加载行为全部写在findClass内部,在loadClass方法的双亲委派链中会使用findClass来查找类
     * <p>
     * <b>启动类加载器(Bootstrap ClassLoader)</b>
     * <b>扩展类加载器(Extension ClassLoader)</b>
     * <b>应用程序类加载器(Application ClassLoader)</b>
     * <p>
     * 我们这里重写一下这个方法来干预一下我们加载类的行为,我们直接加载我们自己写的HashMap类,而不加载jdk的HashMap
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //
        synchronized (getClassLoadingLock(name)) {
            // 先检查是否已经加载
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }

            // 这里我们只干预HashMap的加载方式,并不从父类加载,而是直接在我们自己写的一个jar包中加载一个空类
            if (!name.startsWith("java.util.HashMap")) {
                // 在java.lang.ClassLoader.loadClass(java.lang.String, boolean)这个方法内,可以看到双亲委派模型
                // 1.首先检查是否已经加载对应的类信息 2.如果没有加载交给父类加载器(一直往上递交) 3.父类加载器如果找不到,则由当前的加载器加载
                return super.loadClass(name, resolve);
            }

            // 从指定 JAR 包加载类
            try {
                return findClass(name);
            } catch (Exception e) {
                throw new ClassNotFoundException("Failed to load class: " + name, e);
            }
        }
    }
}
