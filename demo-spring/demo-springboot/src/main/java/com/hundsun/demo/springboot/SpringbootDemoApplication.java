package com.hundsun.demo.springboot;

import lombok.extern.slf4j.Slf4j;
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
        "com.hundsun.demo.springboot.mapper",
})
@org.mybatis.spring.annotation.MapperScan(basePackages = {
        "com.hundsun.demo.springboot.mybatisplus"
})
@Slf4j
// @ServletComponentScan
@EnableScheduling
public class SpringbootDemoApplication {

/*
Spring Boot 读取配置文件的顺序如下：

1. 命令行参数：可以通过命令行参数的方式指定配置文件的路径。例如，使用 `java -jar myproject.jar --spring.config.location=file:/etc/myproject/application.yml` 指定配置文件路径。
2. `SPRING_CONFIG_LOCATION` 环境变量：Spring Boot 会优先读取该环境变量所指定的配置文件路径。
3. 项目根目录下的 `config/` 目录：Spring Boot 会读取项目根目录下的 `config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
4. 项目根目录：Spring Boot 会读取项目根目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。
5. `classpath:/config/` 目录：Spring Boot 会读取 `classpath:/config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
6. `classpath:/` 目录：Spring Boot 会读取 `classpath:/` 目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。

注：优先级从高到低排列。如果同一配置文件存在于多个位置，则优先级高的位置会覆盖优先级低的位置的配置。同时，高优先级的位置中的配置文件也可以包含低优先级位置中的配置文件。例如，项目根目录下的 `application.properties` 可以使用 `@PropertySource("classpath:custom.properties")` 引入 `custom.properties` 文件中的配置。
 */

    /*
     *
     * Spring Boot内嵌 Tomcat 的实现原理主要涉及以下几个步骤:
     * 1. 添加依赖: 在 Spring Boot 项目中, 我们通常会添加 spring-boot-starter-web 这个 starter, 它在 pom.xml 中包含了一些依赖, 包括 web、webmvc和 tomcat 等。
     * 2. 自动配置: Spring Boot 会自动配置 Web服务器。在spring-boot-autoconfigure模块中，有处理关于 WebServer 的自动配置类 ServletWebServerFactoryAutoConfiguration。
     *      这个自动配置类会导入嵌入式容器相关的自动配置类, 如 EmbeddedTomcat、EmbeddedJetty和 EmbeddedUndertow。
     * 3. 创建 WebServer: 在刷新Spring上下文的过程中, Spring Boot会创建 WebServer。具体来说, 当 Spring Boot 应用启动时, 它会创建一个 ServletWebServerApplicationContext, 这个上下文会创建一个内嵌的 Servlet 容器
     * 4. 启动 Tomcat: 在创建WebServer的过程中, Spring Boot 会根据你的 classpath 和你定义的配置来决定使用哪种 Servlet 容器(默认是Tomcat), 然后实例化这个容器, 并将其封装在一个 WebServer 接口的实现类中。
     * 总的来说, Spring Boot内嵌 Tomcat 的过程实际上并不复杂, 就是在刷新 Spring 上下文的过程中将 Tomcat容器启动起来, 并且将当前应用绑定到一个 Context, 然后添加了 Host。
     */
    public static void main(String[] args) {
        // System.setProperty("cglib.debugLocation","C:\\Project\\study-demo\\demo-spring\\demo-springboot\\target\\classes");
        ApplicationContext applicationContext = SpringApplication.run(SpringbootDemoApplication.class);
        log.info("启动完成");
    }
}
