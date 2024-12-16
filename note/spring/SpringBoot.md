## Spring Boot 启动流程

Spring Boot 的启动流程是一个复杂的过程，它通过自动配置、依赖注入、环境准备等机制，将应用程序从启动到运行的整个过程进行简化和优化。以下是 Spring Boot 启动的主要流程分解：

### 1. **启动入口**

- Spring Boot 应用程序的启动通常从一个带有 `@SpringBootApplication` 注解的主类开始。这个类通常包含一个 `main` 方法，调用 `SpringApplication.run(YourApplication.class, args)` 来启动应用程序。

### 2. **SpringApplication 实例创建**

- `SpringApplication.run` 方法内部首先会创建一个 `SpringApplication` 实例。这个实例负责管理应用的启动流程。
- 在实例创建时，Spring Boot 会自动判断应用是 Web 应用还是普通的非 Web 应用，并进行相应的配置。

### 3. **环境准备**

- `SpringApplication` 会准备一个 `Environment` 实例，用于保存和配置应用程序的环境信息，比如系统属性、环境变量、配置文件等。
- 这个阶段会处理命令行参数、加载默认的配置文件（如 `application.properties` 或 `application.yml`），并将它们合并到 `Environment` 中。

### 4. **应用上下文创建**

- 根据应用的类型（Web、非 Web），Spring Boot 会创建相应的 `ApplicationContext`（如 `AnnotationConfigApplicationContext` 或 `WebApplicationContext`）。
- 应用上下文是 Spring 容器的核心，它负责管理 Bean 的创建、初始化和销毁等生命周期。

### 5. **准备上下文**

- Spring Boot 会配置并刷新 `ApplicationContext`，执行所有的 `SpringApplicationRunListeners` 和 `ApplicationContextInitializers`。
- 在这个阶段，还会进行 Bean 的定义、加载和注册。

### 6. **加载自动配置类**

- Spring Boot 的核心特性之一是自动配置。在这个阶段，`SpringFactoriesLoader` 会加载所有在 `META-INF/spring.factories` 文件中配置的自动配置类，并将它们注入到应用上下文中。
- 自动配置类基于当前的 `Environment` 和类路径中的内容，按需配置各个组件和服务。

### 7. **启动类的初始化**

- `@SpringBootApplication` 注解中的 `@ComponentScan` 会扫描并注册所有的 Spring 组件（包括 `@Controller`、`@Service`、`@Repository`、`@Component` 等）到 Spring 容器中。
- 如果定义了自定义的 `CommandLineRunner` 或 `ApplicationRunner`，它们也会在这个阶段被执行。

### 8. **事件发布**

- 在启动过程中，Spring Boot 会发布各种事件（如 `ApplicationStartingEvent`、`ApplicationEnvironmentPreparedEvent`、`ApplicationPreparedEvent`、`ApplicationStartedEvent` 等）。
- 这些事件可以通过事件监听器进行捕获，用于执行一些定制化的逻辑。

### 9. **启动 Tomcat/Jetty/Web 服务器（对于 Web 应用）**

- 如果是 Web 应用，Spring Boot 会自动启动嵌入式的 Tomcat、Jetty 或其他 Web 服务器，并将 Spring 的 `DispatcherServlet` 注册到服务器中，处理所有的 HTTP 请求。

### 10. **完成启动**

- 当应用上下文初始化完毕，并且所有的 Bean 都已准备就绪时，Spring Boot 会发布 `ApplicationReadyEvent`，表示应用已经准备好接收请求。
- 此时，Spring Boot 应用已经成功启动，可以对外提供服务。

### 11. **运行和关闭**

- 应用启动后，Spring Boot 会保持运行状态，直到接收到停止信号。应用关闭时，Spring Boot 会执行所有的 `DisposableBean` 和 `@PreDestroy` 注解标记的方法，进行资源的清理和关闭操作。

### 总结

Spring Boot 的启动流程是一个高度自动化的过程，它通过创建 `SpringApplication` 实例来启动应用程序，准备环境和上下文，加载自动配置，启动 Web 服务器，最终完成应用的启动。这个过程包含了丰富的扩展点和配置选项，使开发者能够轻松构建和运行 Spring 应用程序。



## Spring Boot 启用 Https 

```shell
# 通过这个生成一个密钥
keytool -keystore mykeys.jks -genkey -alias tomcat -keyalg RSA
# 我这里密码设置了 123456
# 完成后得到一个 mykeys.jks 文件
```

放入资源目录下，在配置文件中开启如下配置之后就开启了 Https

```properties
server.ssl.key-store=classpath:mykeys.jks
server.ssl.key-store-password=123456
server.ssl.key-password=123456
```

