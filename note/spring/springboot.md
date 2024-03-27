### 什么是springboot

Spring Boot 和传统的 Spring 框架相比，有以下几个显著的不同之处：

1. **自动配置和约定优于配置**：
    - Spring Boot 强调约定优于配置的理念，提供了大量的自动配置，简化了开发者的配置工作。相比之下，传统的 Spring 框架需要开发者手动配置大量的 XML 或 Java 配置文件来配置应用程序。

2. **起步依赖**：
    - Spring Boot 提供了一系列的“起步依赖”，这些依赖库封装了特定场景下常用的库和框架，使得开发者可以更加轻松地集成这些功能到自己的应用程序中。传统的 Spring 框架需要开发者手动引入所需的依赖，并且可能需要处理依赖之间的冲突和版本兼容性问题。

3. **嵌入式 Web 服务器**：
    - Spring Boot 默认集成了嵌入式的 Tomcat、Jetty 或 Undertow 服务器，使得开发者可以将应用程序打包成一个独立的 JAR 文件，并直接运行在内置的 Web 服务器上，而不需要部署到外部容器。传统的 Spring 框架通常需要部署到外部的 Servlet
      容器中，如 Tomcat、Jetty 或者 JBoss。

4. **Actuator**：
    - Spring Boot 提供了 Actuator 模块，通过它可以监控和管理 Spring Boot 应用程序，包括查看应用程序的健康状态、配置信息、性能指标等。传统的 Spring 框架中没有提供这样的功能，开发者需要自己实现监控和管理功能。

5. **更加简化的配置**：
    - Spring Boot 提供了许多默认配置，开发者只需要根据需要进行必要的配置即可，大大减少了配置的工作量。传统的 Spring 框架需要开发者手动配置大量的细节，配置相对繁琐。

总的来说，Spring Boot 在简化配置、提供起步依赖、集成嵌入式 Web 服务器以及提供 Actuator 等方面相较于传统的 Spring 框架更加便捷和高效。

### springboot有哪些核心注解

Spring Boot 的核心注解包括但不限于以下几个：

1. **@SpringBootApplication**：
    - 这是 Spring Boot 应用程序的入口注解，通常用于主应用程序类上。它整合了 `@Configuration`、`@EnableAutoConfiguration` 和 `@ComponentScan` 注解，标识该类是 Spring Boot
      应用程序的主配置类，并启用自动配置功能。

2. **@RestController**：
    - 这个注解用于标识一个类为 RESTful 风格的控制器，通常用于 Spring Boot 中的 REST API 控制器类。它将类中的每个方法的返回值直接作为 HTTP 响应体返回给客户端，而不需要额外的视图解析器。

3. **@Autowired**：
    - 这个注解用于自动装配 Spring 容器中的 bean。通过在成员变量、构造函数、Setter 方法上标记 `@Autowired` 注解，Spring Boot 将会自动在容器中查找匹配的 bean，并将其注入到标记了 `@Autowired` 的位置。

4. **@Component**：
    - 这是 Spring Framework 的核心注解之一，用于标识一个类为 Spring 组件（Component）。在 Spring Boot 中，常用于标识需要被 Spring 自动扫描并加入到 Spring 容器中管理的类。

5. **@Service**、**@Repository**、**@Controller**：
    - 这些注解分别用于标识一个类为服务层组件、持久层组件和控制器组件。它们都是 Spring Framework 中的衍生注解，通常与 `@Component` 注解一起使用。

6. **@Configuration**：
    - 这个注解用于标识一个类为配置类，通常与 `@Bean` 注解一起使用，用于定义 Spring Bean 的配置信息。

7. **@Bean**：
    - 这个注解用于在配置类中定义 Spring Bean，并将其加入到 Spring 容器中管理。

8. **@Value**：
    - 这个注解用于将配置文件中的属性值注入到 Spring Bean 的成员变量中，从而实现配置项的外部化管理。

9. **@RequestMapping**、**@GetMapping**、**@PostMapping** 等：
    - 这些注解用于映射 HTTP 请求路径到控制器的方法上，实现请求的处理和路由。

这些注解是 Spring Boot 中常用的核心注解，它们帮助开发者快速构建和配置 Spring Boot 应用程序。

### spring自动配置的原理

当Spring Boot应用程序启动时，它会利用`@EnableAutoConfiguration`注解启用自动配置功能。这个注解告诉Spring Boot根据类路径、依赖关系和其他因素来自动配置应用程序。

自动配置的原理如下：

1. **条件化配置**：Spring Boot使用条件化配置来决定是否应该应用某项配置。条件化配置使用`@Conditional`注解，它根据条件来确定是否应该创建一个特定的bean或应用一个特定的配置。

2. **自动配置类**：Spring Boot包含许多自动配置类，这些类负责根据条件自动配置应用程序。这些类使用`@Configuration`注解标记，并且通常使用条件化注解来控制它们的应用条件。

3. **类路径扫描**：Spring Boot会扫描类路径下的依赖和配置，并根据这些信息加载自动配置类。这包括查找`META-INF/spring.factories`文件中的配置，这些文件包含自动配置类的定义。

4. **外部化配置**：Spring Boot支持将配置信息放在外部文件中，如`application.properties`或`application.yml`。应用程序启动时，它会加载这些文件，并根据文件中的属性值进行自动配置。

通过这些机制，Spring Boot能够根据应用程序的环境和配置情况自动配置应用程序的各种组件，从而简化开发过程，提高开发效率。

### 如何理解springboot的starter模块

Spring Boot 的 Starter 模块是一种用于简化依赖管理的特殊方式。它通过将常用的依赖组合在一起，为特定功能领域提供了一站式解决方案。理解 Spring Boot Starter 模块的关键点如下：

1. **封装依赖**：Starter 模块封装了特定功能领域的依赖。例如，`spring-boot-starter-web` 包含了构建 Web 应用程序所需的所有依赖，包括 Spring MVC、Tomcat 等。这样，开发者只需引入一个 Starter 模块，即可快速构建出符合需求的项目。

2. **约定优于配置**：Starter 模块遵循 Spring Boot 的约定优于配置的理念。通过约定好的依赖组合，它简化了项目的配置过程。开发者无需手动引入每一个依赖，也不必担心依赖之间的版本兼容性问题。

3. **简化配置**：Starter 模块通常还提供了默认的配置，以便快速启动项目。这些默认配置可以让开发者立即开始工作，而不必花费大量时间在配置文件上。

4. **可扩展性**：虽然 Starter 模块提供了一套默认的依赖和配置，但开发者仍然可以根据需要进行定制和扩展。他们可以覆盖默认配置，引入其他依赖，或者添加自定义配置来满足特定需求。

5. **命名规范**：Spring Boot Starter 模块的命名遵循一定的规范，通常以 `spring-boot-starter-` 作为前缀，后跟功能或领域的名称，如 `spring-boot-starter-data-jpa`
   、`spring-boot-starter-security` 等。

总之，Spring Boot Starter 模块通过封装常用的依赖和默认配置，简化了项目的搭建和配置过程，提高了开发效率，使得开发者能够更专注于业务逻辑的实现。

### 如何在 Spring Boot 启动的时候运行一些特定的代码

在 Spring Boot 启动时运行特定的代码通常可以通过实现 `ApplicationRunner` 或 `CommandLineRunner` 接口来实现。这两个接口都是 Spring Boot 提供的用于在应用程序启动后执行特定代码的方式，它们的区别在于参数的不同。

1. **ApplicationRunner**：
    - `ApplicationRunner` 接口中的 `run` 方法带有一个 `ApplicationArguments` 参数，可以用于访问启动应用程序时传递的命令行参数。这使得 `ApplicationRunner` 更适合处理命令行参数相关的任务。
    - 示例代码：
   ```java
   import org.springframework.boot.ApplicationArguments;
   import org.springframework.boot.ApplicationRunner;
   import org.springframework.stereotype.Component;

   @Component
   public class MyApplicationRunner implements ApplicationRunner {

       @Override
       public void run(ApplicationArguments args) throws Exception {
           // 在应用程序启动后执行的代码
           System.out.println("ApplicationRunner executed");
       }
   }
   ```

2. **CommandLineRunner**：
    - `CommandLineRunner` 接口中的 `run` 方法带有一个字符串数组参数，可以用于访问启动应用程序时传递的命令行参数。这使得 `CommandLineRunner` 更适合处理简单的字符串数组参数。
    - 示例代码：
   ```java
   import org.springframework.boot.CommandLineRunner;
   import org.springframework.stereotype.Component;

   @Component
   public class MyCommandLineRunner implements CommandLineRunner {

       @Override
       public void run(String... args) throws Exception {
           // 在应用程序启动后执行的代码
           System.out.println("CommandLineRunner executed");
       }
   }
   ```

除了实现 `ApplicationRunner` 或 `CommandLineRunner` 接口外，还有其他几种方式可以在 Spring Boot 启动时执行特定的代码：

3. **使用 ApplicationListener**：
    - 可以实现 `ApplicationListener` 接口，监听 `ApplicationStartedEvent` 或 `ApplicationReadyEvent` 事件，在应用程序启动或准备就绪时执行特定代码。
    - 示例代码：
   ```java
   import org.springframework.boot.context.event.ApplicationStartedEvent;
   import org.springframework.context.ApplicationListener;
   import org.springframework.stereotype.Component;

   @Component
   public class MyApplicationListener implements ApplicationListener<ApplicationStartedEvent> {

       @Override
       public void onApplicationEvent(ApplicationStartedEvent event) {
           // 在应用程序启动时执行的代码
           System.out.println("ApplicationListener executed");
       }
   }
   ```

4. **使用 Spring Boot 的事件机制**：
    - 可以通过 `SpringApplication` 的 `addListeners()` 方法注册事件监听器，监听 Spring Boot 的各种事件，并在事件触发时执行特定代码。
    - 示例代码：
   ```java
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.boot.context.event.ApplicationStartedEvent;
   import org.springframework.context.ApplicationListener;

   @SpringBootApplication
   public class MyApplication {

       public static void main(String[] args) {
           SpringApplication application = new SpringApplication(MyApplication.class);
           application.addListeners((ApplicationListener<ApplicationStartedEvent>) event -> {
               // 在应用程序启动时执行的代码
               System.out.println("Event listener executed");
           });
           application.run(args);
       }
   }
   ```

5. **使用 `@PostConstruct` 注解**：
    - 可以在 Spring Bean 中使用 `@PostConstruct` 注解标记一个方法，在 Bean 初始化完成后执行特定的代码。
    - 示例代码：
   ```java
   import javax.annotation.PostConstruct;
   import org.springframework.stereotype.Component;

   @Component
   public class MyBean {

       @PostConstruct
       public void init() {
           // 在 Bean 初始化完成后执行的代码
           System.out.println("@PostConstruct executed");
       }
   }
   ```

这些方法可以根据需求选择，实现在 Spring Boot 启动时执行特定的代码。

### springboot的热部署