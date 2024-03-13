package com.hundsun.demo.spring.jdk.spi;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.java.jdk.spi
 * @className: Translate
 * @description:
 * @author: h1123
 * @createDate: 2023/2/5 1:32
 */

public interface Translate {

    /*
    SPI - Service Provider Interface
    JDK内置的一种服务提供发现机制, 可以用来启动框架和替换组件.
    SPI的应用场景有 JDBC, Common-Logging, Dubbo, SpringBoot自动配置

    SPI的流程
    1. 定义接口标准(就像java.sql.Driver接口)
    2. 第三方提供具体的实现: 实现具体方法并配置 META-INF/service/${interface_name} 文件, 这个文件里的内容就是具体的实现类的全限定类名
    3. 开发着使用: 引入第三方提供的jar包, 通过JDK查找服务的实现的工具类 - java.util.ServiceLoader 来使用到该服务
     */

    /**
     * SPI接口对外提供的方法, 以供服务调用方实现该方法, 这里模拟一个翻译场景
     */
    public void translate();
}
