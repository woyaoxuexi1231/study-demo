## Spring Boot 配置文件加载顺序

Spring Boot 读取配置文件的顺序如下：

1. 命令行参数：可以通过命令行参数的方式指定配置文件的路径。例如，使用 `java -jar myproject.jar --spring.config.location=file:/etc/myproject/application.yml` 指定配置文件路径。
2. `SPRING_CONFIG_LOCATION` 环境变量：Spring Boot 会优先读取该环境变量所指定的配置文件路径。
3. 项目根目录下的 `config/` 目录：Spring Boot 会读取项目根目录下的 `config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
4. 项目根目录：Spring Boot 会读取项目根目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。
5. `classpath:/config/` 目录：Spring Boot 会读取 `classpath:/config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
6. `classpath:/` 目录：Spring Boot 会读取 `classpath:/` 目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。

注：优先级从高到低排列。如果同一配置文件存在于多个位置，则优先级高的位置会覆盖优先级低的位置的配置。同时，高优先级的位置中的配置文件也可以包含低优先级位置中的配置文件。例如，项目根目录下的 `application.properties`
可以使用 `@PropertySource("classpath:custom.properties")` 引入 `custom.properties` 文件中的配置。

`bootstrap.yml` 是 Spring Boot 应用程序的一个特殊的配置文件，用于在应用程序启动之前加载。它的作用是在 Spring 应用程序的上下文创建之前加载外部配置，通常用于一些框架的初始化和配置，例如连接配置中心、加密/解密属性等。
与 `application.yml` 或 `application.properties` 不同，`bootstrap.yml` 的加载顺序更早，因此可以用来配置一些在应用程序启动之前就需要用到的参数。
它的配置格式与 `application.yml` 或 `application.properties` 类似，但主要用于一些与应用程序启动相关的配置。
一般情况下，`bootstrap.yml` 用于配置一些基础设施相关的配置，例如配置中心、分布式追踪、加密/解密等。

当你使用配置中心时，配置中心的配置会在应用程序启动时被加载，并且它的加载优先级高于本地的配置文件。加载顺序如下：

1. **配置中心的配置被加载**：
    - 如果你启用了配置中心（例如使用 Consul、Spring Cloud Config 等），应用程序会首先尝试从配置中心获取配置。这些配置会在应用程序启动时被加载，并覆盖本地配置文件中相同键的配置。

2. **本地配置文件的配置被加载**：
    - 如果配置中心中不存在某些配置项，或者配置中心不可用，应用程序会回退到本地的配置文件中查找配置。本地配置文件中的配置会在配置中心加载之后被加载，但在没有配置中心的情况下，本地配置文件中的配置会在应用程序启动时被最先加载。

综上所述，配置中心的配置会在本地配置文件之前被加载，并覆盖本地配置文件中相同键的配置。这种加载顺序保证了配置中心的配置可以被应用程序灵活地管理和更新，同时也提供了一种在本地快速启动应用程序的方式。


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

## Spring Aop 

### aop的各种概念

AOP（Aspect-Oriented Programming，面向切面编程）是一种软件开发方法，旨在通过将横切关注点（cross-cutting concerns）从核心业务逻辑中分离出来，以提高代码的模块化性、可维护性和可重用性。以下是 AOP 中常见的概念：

1. **切面（Aspect）：**
   切面是一个模块化单元，它横切应用程序的多个类。它封装了与特定横切关注点（如日志、事务管理、安全性等）相关的行为，并可以在程序中的多个位置重复使用。切面可以包含通知以及切点。  
   代表的是系统中由 Pointcut 和 Advice 组合而成的一个 AOP 概念实体
2. **通知（Advice）：**
   通知是切面的具体行为。在 AOP 中，通知定义了在何时、何地、以及如何应用切面的行为。常见的通知类型包括：
    - **前置通知（Before advice）：** 在连接点之前执行的通知。
    - **后置通知（After advice）：** 在连接点完成后（无论是正常返回还是异常退出）执行的通知。
    - **返回通知（After returning advice）：** 在连接点正常完成后执行的通知。
    - **异常通知（After throwing advice）：** 在方法抛出异常后执行的通知。
    - **环绕通知（Around advice）：** 包围连接点的通知，在方法调用之前和之后都可以执行自定义的行为。

   表示在这个 JoinPoint 需要添加的横切逻辑, 对应我们的 @Before, @After, @Around
3. **连接点（Join Point）：**
   连接点是程序执行的特定点，如方法调用、方法执行、异常处理等。在 AOP 中，连接点被定义为在应用程序中允许插入切面的点。  
   在确切的某个点进行织入操作的那个点就是一个 JoinPoint
4. **切点（Pointcut）：**
   切点是指一组连接点的集合，允许开发者定义在哪些连接点应用通知。通知只在与切点匹配的连接点上被执行。
   JoinPoint 是我们逻辑上的一个点, 而 Pointcut 在代码层面帮我们描述 JoinPoint 且 Pointcut可以描述由一堆 JoinPoint 的集合, SpringAop 中的 @PointCut 注解
5. **引入（Introduction）：**
   引入允许向现有类添加新方法或属性。在 AOP 中，引入允许切面向现有的类添加新功能，而无需修改它们的代码。

6. **织入（Weaving）：**
   织入是将切面与应用程序的目标对象连接起来并创建新的代理对象的过程。织入可以在编译时、加载时或运行时完成。在 Spring 中，AOP 通常通过动态代理或者字节码增强来实现织入。

7. **Weaver（织入器）：**
   Weaver 是 AOP 中负责将切面与目标对象连接起来并创建代理对象的组件。它负责将切面的通知逻辑应用到目标对象的连接点上，从而实现横切关注点的织入。Weaver 可以在编译时、加载时或者运行时进行织入操作。常见的织入方式包括静态代理、动态代理和字节码增强。在 Spring AOP
   中，Weaver 负责将切面织入到 Spring 容器管理的 Bean 上，以创建代理对象来实现切面功能。

8. **Target Object（目标对象）：**
   Target Object 是指在 AOP 中被切面所影响或增强的原始对象。它是应用程序中实际执行业务逻辑的对象。通常，切面中的通知会围绕着目标对象的连接点来执行相应的操作，例如在方法调用前后添加日志、在方法执行时处理事务等。目标对象可以是任何普通的 Java 对象，包括 POJO（Plain
   Old Java Object）、Spring 管理的 Bean 等。

这些概念共同构成了 AOP 的核心，通过 AOP，开发者可以将横切关注点与核心业务逻辑分离，提高了代码的可维护性和可重用性。

### spring aop

Spring AOP（Aspect-Oriented Programming，Spring面向切面编程）是 Spring 框架提供的一种基于 AOP 思想的编程方式，旨在使开发者能够更轻松地实现横切关注点的功能，并将其与核心业务逻辑解耦。下面是关于 Spring AOP 的详细介绍：

1. **概念：**
   Spring AOP 是 Spring 框架提供的 AOP 实现，它允许开发者通过定义切面（Aspect）和通知（Advice）来实现横切关注点的功能，而无需直接修改核心业务逻辑。

2. **核心组件：**
    - **切面（Aspect）：** 切面是一个模块化单元，它封装了与特定横切关注点相关的行为，例如日志记录、事务管理、安全性等。
    - **通知（Advice）：** 通知定义了在何时、何地、以及如何应用切面的行为。Spring AOP 支持常见的通知类型，包括前置通知、后置通知、返回通知、异常通知和环绕通知。
    - **切点（Pointcut）：** 切点是一组连接点的集合，它允许开发者定义在哪些连接点应用通知。
    - **织入器（Weaver）：** 织入器负责将切面中定义的通知织入到目标对象的连接点上，从而实现横切关注点的功能。
    - **目标对象（Target Object）：** 目标对象是应用程序中被切面所影响或增强的原始对象，它是实际执行业务逻辑的对象。

3. **支持的通知类型：**
    - **前置通知（Before advice）：** 在连接点之前执行的通知。
    - **后置通知（After advice）：** 在连接点完成后（无论是正常返回还是异常退出）执行的通知。
    - **返回通知（After returning advice）：** 在连接点正常完成后执行的通知。
    - **异常通知（After throwing advice）：** 在方法抛出异常后执行的通知。
    - **环绕通知（Around advice）：** 包围连接点的通知，在方法调用之前和之后都可以执行自定义的行为。

4. **Spring AOP vs. AspectJ：**
   Spring AOP 是基于动态代理实现的轻量级 AOP 框架，它仅支持方法级别的连接点。相比之下，AspectJ 是一个功能更强大的 AOP 框架，它支持更广泛的连接点，包括方法调用、字段访问、对象创建等。Spring AOP 在运行时创建代理对象，而 AspectJ
   可以在编译时或者加载时进行织入操作。

5. **使用场景：**
    - 记录日志：在方法执行前后记录日志信息。
    - 事务管理：在方法执行前后开启、提交或回滚事务。
    - 权限控制：在方法执行前检查用户权限。
    - 缓存管理：在方法执行前后存取缓存数据。
    - 异常处理：在方法执行后处理异常情况。

总的来说，Spring AOP 提供了一种轻量级的 AOP 实现，使开发者能够更加灵活地管理应用程序的横切关注点，从而提高代码的模块化性、可维护性和可重用性。

### spring aop

在 Spring AOP 中，有一些核心的类和接口起到了关键作用，以下是其中比较重要的一些类和接口：

1. **Aspect（切面）：**
    - **@Aspect：** 这是一个注解，用于定义一个切面。通常与其他注解（如 @Before、@After、@Around 等）一起使用，用于定义通知和切点。

2. **Advice（通知）：**
    - **MethodBeforeAdvice：** 前置通知的接口，用于在方法调用之前执行。
    - **AfterReturningAdvice：** 返回通知的接口，用于在方法正常返回后执行。
    - **ThrowsAdvice：** 异常通知的接口，用于在方法抛出异常后执行。
    - **MethodInterceptor：** 环绕通知的接口，用于在方法调用前后执行自定义的行为。

3. **Pointcut（切点）：**
    - **Pointcut：** 定义了一组连接点，通知将会在这些连接点上被执行。Spring 提供了多种实现，如 `NameMatchMethodPointcut`、`RegexpMethodPointcut` 和 `JdkRegexpMethodPointcut` 等。

4. **Advisor（顾问）：**
    - **DefaultPointcutAdvisor：** 默认的顾问类，用于将切点和通知结合起来。

5. **ProxyFactoryBean：**
    - **ProxyFactoryBean：** 用于创建代理对象的工厂类，可以通过它的属性设置来定义代理对象的行为，如要代理的目标对象、要织入的切面等。

6. **ProxyFactory：**
    - **ProxyFactory：** 与 `ProxyFactoryBean` 类似，也是用于创建代理对象的工厂类，但它更为底层，可以更加灵活地创建代理对象。

7. **AopProxy：**
    - **AopProxy：** 是一个接口，定义了获取代理对象的方法，有两个实现类 `JdkDynamicAopProxy` 和 `CglibAopProxy`，分别用于基于 JDK 动态代理和 CGLIB 代理的情况。

8. **ProxyConfig：**
    - **ProxyConfig：** 代理配置类，用于配置代理对象的创建方式和其他相关属性。

这些类和接口构成了 Spring AOP 的核心组件，通过它们，开发者可以定义切面、通知、切点和代理对象，从而实现横切关注点的功能，并将其与核心业务逻辑解耦。

### AnnotationAwareAspectJAutoProxyCreator

`AnnotationAwareAspectJAutoProxyCreator` 是 Spring 框架中的一个类，用于支持基于注解的 AspectJ 切面编程。它是自动代理创建器的一种，特别负责识别带有 AspectJ 注解 (`@Aspect`) 的类，并为它们自动创建代理。这个过程是
AOP (面向切面编程) 的核心组成部分，允许开发者定义切面（aspects）、通知（advises）、切点（pointcuts）等，以便在不修改源代码的情况下增加额外的行为（比如日志记录、事务管理等）。

具体来说，`AnnotationAwareAspectJAutoProxyCreator` 执行以下关键任务：

1. **检测`@Aspect`注解**：它会扫描应用上下文中所有的 Bean，寻找带有 `@Aspect` 注解的类，这些类定义了切面的规则和逻辑。

2. **创建代理**：对于那些与切点表达式匹配的 Bean，`AnnotationAwareAspectJAutoProxyCreator` 会为它们创建代理。这些代理在目标方法执行前后可以执行额外的逻辑，如前置通知（Before advice）、后置通知（After returning
   advice）、异常通知（After throwing advice）等。

3. **管理通知和切点**：它还负责处理这些切面中定义的通知（advice）和切点（pointcut）逻辑，确保在正确的时间、正确的地点执行预定的操作。

`AnnotationAwareAspectJAutoProxyCreator` 是 Spring AOP 功能的关键组件之一，使得在 Spring 应用中实现面向切面编程变得更加简单和直接。通过使用 AspectJ 的注解，开发者可以非常灵活地定义横切关注点，而无需深入了解复杂的 AOP
概念或代理机制。

这个类属于 Spring AOP 和 AspectJ 支持的一部分，通常是通过配置（如使用 Java 配置或 XML 配置）自动注册和激活的，而开发者主要关注于定义切面和通知逻辑。


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