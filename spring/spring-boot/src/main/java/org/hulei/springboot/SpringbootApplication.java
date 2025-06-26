package org.hulei.springboot;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.spring.config.PropertiesConfig;
import org.hulei.springboot.spring.listener.MyApplicationReadyEvent;
import org.hulei.springboot.spring.scope.ProtoTypeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * spring 实战(第五版)
 *
 * @author hulei
 * @since 2024/10/13 21:51
 */

/*
1. 指定这个类为配置类
2. 启动 spring 自动配置
3. 启动组件扫描, 这将默认扫描这个类所在的包及其子包下所有的bean
 */
@ServletComponentScan(basePackages = {"org.hulei.springboot.spring.mvc.filter.webfilter"})
/*
作为 springboot 启动类的默认注解，包含 ComponentScan 注解，默认扫描当前配置类所在包及其子包。
 */
@SpringBootApplication
@Data
@RestController("/application")
@Slf4j
// @EnableDynamicDataSource
public class SpringbootApplication implements ApplicationRunner {

    public static ApplicationContext applicationContext;

    /**
     * 注解 @Value 遇到 static 类型变量会直接失效
     * <p>
     * springBoot 读取配置文件时会把大驼峰 (propertiesName) 转换为 小写+横线(properties-name) 的形式存储
     * 具体代码在 org.springframework.boot.context.properties.bind.JavaBeanBinder.BeanProperty 内
     * this.name = DataObjectPropertyName.toDashedForm(name);
     * this.declaringClassType = declaringClassType;
     * 主要是这个方法 DataObjectPropertyName.toDashedForm
     */
    @Value("${server.port}")
    String port;

    @Value("${greeting.welcome}")
    String greetingWelcome;

    public static void main(String[] args) {
        // System.setProperty("cglib.debugLocation","C:\\Project\\study-demo\\demo-spring\\demo-springboot\\target\\classes");
        // spring.profiles.active 这个配置会激活外部jar包内的配置文件
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
        System.out.println(applicationContext.getEnvironment().getProperty("greeting.welcome"));


        applicationContext.publishEvent(new MyApplicationReadyEvent("容器启动完成!"));
        /*
        内存参数 -Xmx40m -Xms40m
        启动报错了  java.lang.OutOfMemoryError: GC overhead limit exceeded, 分配的内存太少,不停的GC来创建新的对象然而GC又回收不了对象就导致报错了

        -XX:+PrintCommandLineFlags 可以打印JVM 启动时所有的命令行参数，包括默认值和用户自定义值
        启动之后可以使用 jps -l 和 jmap -heap <pid> | grep GC 来查看使用的 GC

        -XX:+UseG1GC 开启G1垃圾收集器

        -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+UseG1GC

        jps 列出所有Java进程
        jstat gc,gc内存相关
        jstack 栈相关,可以排查死锁问题
        -l 长列表模式,显示有关锁的附加信息,如果线程持有某些锁或等待锁,或阻塞于某些对象,日志会显出
        jmap 看堆内存,比jstat详细多了,而且可读性更高
        -heap
        -dump
        jinfo 主要查看jvm启动时的参数使用
        jconsole 我这里linux打不开,在windows本地启动jconsole来玩,需要在linux启动Java程序的时候加上参数
                nohup java -Djava.rmi.server.hostname=192.168.80.128 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3214 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar springboot.jar &
                然后本地的jconsole使用3214这个端口就可以连接上了
        jhat 主要的作用就是分析jmap导出的dump日志,非常详细,还可以看对象被谁引用了

        对比jstack和idea自带的thread dump分析,以及arthas的线程dump分析,后两者更加详细,类似 org.hulei.jdk.root.juc.ReentrantLockTest的情形,会详细显示出Lock被谁持有这,jstack是不行的
        "main" Id=1 WAITING on java.util.concurrent.locks.ReentrantLock$NonfairSync@74dff038 owned by "reentrantLock-test-thread" Id=24
        at sun.misc.Unsafe.park(Native Method)
        ...
        通过jstack要得到这个结果比较麻烦,首先需要jmap导出一份dump文件,通过jstack中持有的对象地址去dump文件中找到那个lock对象,再从NonfairSync@74dff038这个对象的exclusiveOwnerThread变量来确定是哪个线程目前持有这个对象

        我这里虚拟机的内存是 3GB,1核心,打包这个项目之后在虚拟机上面跑发现cpu直接99%了,不下来
        直接 top -H -p <pid> 可以查看这个进程内部占用cpu的线程是谁
         */
    }

    @GetMapping("/debug")
    public void applicationApi() {
        System.out.println("debug mode");
    }

    @Bean
    public ThreadPoolExecutor commonPool() {
        return new ThreadPoolExecutor(
                200,
                200,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("commonPool-").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Bean
    public ThreadPoolExecutor singlePool() {
        return new ThreadPoolExecutor(
                1,
                1,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new ThreadFactoryBuilder().setNamePrefix("single-pool-").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public ThreadPoolTaskExecutor commonTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        // executor.setQueueCapacity(this.commonTaskExecutorProperties.getQueueCapacity());
        // executor.setThreadNamePrefix(this.commonTaskExecutorProperties.getThreadNamePrefix());
        // executor.setAllowCoreThreadTimeOut(this.commonTaskExecutorProperties.getAllowCoreThreadTimeout());
        // executor.setKeepAliveSeconds((int)this.commonTaskExecutorProperties.getKeepAliveSeconds().getSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        // 设置时区为东八区（北京时间）
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 获取当前时间
        Date now = new Date();
        // 根据指定时区获取当前时间
        Date nowInTimeZone = new Date(now.getTime() + timeZone.getRawOffset());
        map.put("response-tag", nowInTimeZone);
        return map;
    }

    @Autowired(required = false)
    PropertiesConfig propertiesConfig;


    @Autowired
    ProtoTypeBean protoTypeBean;

    @GetMapping("/protoTypeBeanTest")
    public void protoTypeBeanTest() {
        // for (int i = 0; i < 10; i++) {
        //     ProtoTypeBean bean = SpringbootApplication.applicationContext.getBean(ProtoTypeBean.class);
        //     log.info("获取的多例bean为: {}", bean);
        // }
        log.info("获取的多例bean为: {}", protoTypeBean);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("{}", propertiesConfig);
    }
}
