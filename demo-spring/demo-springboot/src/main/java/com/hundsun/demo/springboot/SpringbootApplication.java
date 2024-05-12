package com.hundsun.demo.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-25 20:20
 */

@SpringBootApplication
@MapperScan(basePackages = {
        "com.hundsun.demo.springboot.common.mapper",
        "com.hundsun.demo.springboot.rabbitmq.consumer.mapper",
        "com.hundsun.demo.springboot.mysql.mapper",
        "com.hundsun.demo.springboot.tkmybatis.mapper"
})
@org.mybatis.spring.annotation.MapperScan(basePackages = {
        "com.hundsun.demo.springboot.mybatisplus.mapper"
})
@Slf4j
// @ServletComponentScan
@EnableScheduling
public class SpringbootApplication {

    public static ApplicationContext applicationContext;

    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        // System.setProperty("cglib.debugLocation","C:\\Project\\study-demo\\demo-spring\\demo-springboot\\target\\classes");
        /*
        Spring Boot内嵌 Tomcat 的实现原理主要涉及以下几个步骤:
        1. 添加依赖: 在 Spring Boot 项目中, 我们通常会添加 spring-boot-starter-web 这个 starter, 它在 pom.xml 中包含了一些依赖, 包括 web、webmvc和 tomcat 等。
        2. 自动配置: Spring Boot 会自动配置 Web服务器。在spring-boot-autoconfigure模块中，有处理关于 WebServer 的自动配置类 ServletWebServerFactoryAutoConfiguration。
           这个自动配置类会导入嵌入式容器相关的自动配置类, 如 EmbeddedTomcat、EmbeddedJetty和 EmbeddedUndertow。
        3. 创建 WebServer: 在刷新Spring上下文的过程中, Spring Boot会创建 WebServer。具体来说, 当 Spring Boot 应用启动时, 它会创建一个 ServletWebServerApplicationContext, 这个上下文会创建一个内嵌的 Servlet 容器
        4. 启动 Tomcat: 在创建WebServer的过程中, Spring Boot 会根据你的 classpath 和你定义的配置来决定使用哪种 Servlet 容器(默认是Tomcat), 然后实例化这个容器, 并将其封装在一个 WebServer 接口的实现类中。
           总的来说, Spring Boot内嵌 Tomcat 的过程实际上并不复杂, 就是在刷新 Spring 上下文的过程中将 Tomcat容器启动起来, 并且将当前应用绑定到一个 Context, 然后添加了 Host。
         */
        applicationContext = SpringApplication.run(SpringbootApplication.class);
        log.info("启动完成");
        // Open the homepage URL in default browser
        String homepageURL = "http://localhost:10001"; // Update with your homepage URL
        System.out.println("Please navigate to: " + homepageURL);

        /*
        内存参数 -Xmx40m -Xms40m
        启动报错了  java.lang.OutOfMemoryError: GC overhead limit exceeded, 分配的内存太少,不停的GC来创建新的对象然而GC又回收不了对象就导致报错了

        -XX:+PrintCommandLineFlags 可以打印JVM 启动时所有的命令行参数，包括默认值和用户自定义值
        启动之后可以使用 jps -l 和 jmap -heap <pid> | grep GC 来查看使用的 GC

        -XX:+UseG1GC 开启G1垃圾收集器
         */
    }
}
