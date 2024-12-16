## 概念

[spring实战(第五版) 要梯子](https://potoyang.gitbook.io/spring-in-action-v5)

### 简要介绍一下spring各个模块

Spring 框架是一个模块化的框架，拥有多个模块，每个模块都专注于不同的功能领域。以下是 Spring 框架中一些主要的模块：

1. **Spring Core Container**：
    - **IoC Container（控制反转容器）**：Spring 的 IoC 容器是 Spring 框架的核心，负责管理应用程序中的对象（Bean）的生命周期、依赖关系、配置和注入。它包括
      BeanFactory 和
      ApplicationContext 两个主要实现。
        - **BeanFactory**
            - `BeanFactory` 是 Spring 框架的核心接口之一，它负责管理应用程序中的对象（Bean）的生命周期、依赖关系、配置和注入。
            - `BeanFactory` 通过将应用程序的配置信息加载到容器中，并使用这些信息实例化、配置和组装 Bean
              对象，从而实现了控制反转（IoC）和依赖注入（DI）。
            - `BeanFactory` 提供了许多实现类，其中最常用的是 `DefaultListableBeanFactory` 和 `XmlBeanFactory`。
        - **ApplicationContext**
            - `ApplicationContext` 是 `BeanFactory` 的子接口，它是 Spring 框架中更高级的容器，提供了更多的功能和特性。
            - `ApplicationContext` 在 BeanFactory 的基础上，添加了对国际化、事件传播、资源加载、AOP 等方面的支持。
            - `ApplicationContext` 实现类包括 `ClassPathXmlApplicationContext`、`FileSystemXmlApplicationContext`
              、`AnnotationConfigApplicationContext` 等。
        - 这两个部分是 Spring Core Container 的核心组件，它们提供了 Spring 框架中最基本的功能，例如 Bean
          的创建、依赖注入、配置管理、生命周期管理等。通过这些功能，Spring Core
          Container
          实现了松耦合、模块化和可测试性，使得应用程序的开发和维护变得更加简单和灵活。
    - **Dependency Injection（依赖注入）**：Spring 使用依赖注入来管理对象之间的依赖关系，使得对象之间的耦合度降低，更易于测试和维护。

2. **Spring AOP（面向切面编程）**：
    - **Aspect（切面）**：切面是一种模块化的方式，用于横向切割关注点（cross-cutting concerns），例如事务管理、日志记录、安全等。
    - **Join point（连接点）**：连接点是在应用程序执行期间可以插入切面的点，例如方法的调用、异常的处理等。
    - **Advice（通知）**：通知是切面在连接点上执行的动作，例如在方法执行前后、抛出异常时等。 前置通知（Before
      advice）、后置通知（After returning advice）、异常通知（After
      throwing advice）、最终通知（After finally
      advice）、环绕通知（Around advice）。
    - **Weaving（织入）**：织入是将切面应用到目标对象的过程，可以在编译时、加载时或运行时进行。

    ```java
    import org.aspectj.lang.annotation.Aspect;
    import org.aspectj.lang.annotation.Before;
    
    // 这整个类就是一个切面
    @Aspect
    public class LoggingAspect {
    
        // 切入点为UserService的所有方法的前置通知
        @Before("execution(* com.example.service.UserService.*(..))")
        public void beforeAdvice() {
            System.out.println("Before method execution");
        }
    }
    
    ```
   @Aspect 注解标记了这个类为一个切面。  
   @Before 注解标记了一个前置通知，指定了切入点表达式 "execution(* com.example.service.UserService.*(..))"，表示所有
   UserService 类中的方法调用作为连接点。  
   在 beforeAdvice() 方法中，我们定义了在方法执行之前输出日志的逻辑。

3. **Spring DAO（数据访问）**：
   Spring DAO 模块是 Spring 框架的一个重要部分，专注于简化数据访问层的开发。它提供了许多实用的功能和技术，使得与数据库交互更加简单、高效和可维护。下面是
   Spring DAO 模块的一些主要特点和功能：
    1. **JDBC 抽象层**：
       Spring DAO 提供了对 JDBC 的抽象，简化了 JDBC 编程的复杂性。它包括了一系列的 JDBC 模板类（例如
       JdbcTemplate、NamedParameterJdbcTemplate），这些模板类提供了许多便捷的方法来执行 SQL
       查询、更新和批处理操作，同时封装了异常处理和资源释放等操作，减少了样板式代码的编写。
    2. **ORM 整合**：
       Spring DAO 提供了与主流的 ORM 框架（例如 Hibernate、JPA）的集成支持，使得在 Spring 应用中使用 ORM 技术变得更加容易。通过
       Spring DAO，可以轻松地将 ORM 框架的功能与
       Spring
       的事务管理、依赖注入等特性无缝整合，从而实现更加灵活和可维护的数据访问层。
    3. **声明式事务管理**：
       Spring DAO 提供了声明式事务管理的支持，使得在 Spring 应用中管理事务变得更加简单和灵活。通过声明式事务管理，开发者可以使用注解或
       XML 配置来定义事务的边界和传播行为，而无需手动编写事务管理代码。
    4. **数据访问异常处理**：
       Spring DAO 提供了一套一致的异常体系用于处理数据访问中的异常，使得在应用程序中统一处理数据库相关的异常变得更加方便和可靠。
    5. **数据访问支持类**：
       Spring DAO 提供了许多支持类和工具类，用于简化数据访问层的开发。例如，它提供了 RowMapper 接口用于将查询结果集映射到
       Java 对象，提供了 SQL 参数的封装类
       SqlParameterSource，提供了 JdbcOperations 接口用于执行 JDBC
       操作等。

   总的来说，Spring DAO 模块通过提供一系列的抽象、工具和技术，使得在 Spring 应用中进行数据访问变得更加简单、灵活和高效。无论是直接使用
   JDBC 进行操作，还是通过集成的 ORM
   框架进行持久化操作，都可以得到很好的支持和便利。

4. **Spring MVC（模型-视图-控制器）**：
    - **DispatcherServlet（调度器 Servlet）**：Spring MVC 的核心组件，负责接收 HTTP 请求并将其分派到相应的处理器（Controller）上进行处理。
    - **HandlerMapping（处理器映射器）**：负责将请求映射到相应的处理器上。
    - **Controller（控制器）**：处理 HTTP 请求，执行业务逻辑，并返回相应的视图。
    - **ViewResolver（视图解析器）**：将逻辑视图名称解析为实际的视图对象。

5. **Spring Security（安全框架）**：Spring Security 是一个功能强大的安全框架，用于保护应用程序的安全性，包括身份验证、授权、会话管理、加密等功能。

6. **Spring Boot（快速应用开发）**：Spring Boot 是 Spring 的一个子项目，旨在简化 Spring
   应用程序的开发和部署。它提供了自动配置、起步依赖、内嵌容器等特性，使得开发者可以更加快速地构建独立的、生产级别的应用程序。

7. **Spring Data（数据访问）**：Spring Data 是一个用于简化数据访问的项目，它为不同的数据存储提供了统一的数据访问抽象，并提供了
   Repository 接口用于简化数据访问层的开发。

8. **Spring Integration（集成框架）**：Spring Integration 是一个用于企业集成的框架，提供了丰富的集成模式和组件，用于处理消息、文件、WebService
   等各种集成场景。

这些是 Spring 框架中的一些主要模块，每个模块都专注于不同的领域，并提供了丰富的功能和特性，使得开发者可以更加轻松地构建复杂的企业级应用程序。

### spring用到了哪些设计模式

Spring 框架在其设计和实现中运用了多种设计模式，其中一些常见的设计模式包括但不限于：

1. **工厂模式**：
   Spring 使用工厂模式来创建和管理对象的生命周期。例如，BeanFactory 和 ApplicationContext 是 Spring 中常用的工厂类，它们负责创建和管理
   bean 对象。

2. **单例模式**：
   Spring 中的 bean 默认是单例的，即每个 bean 在容器中只有一个实例。这样可以节省资源，并且保证了依赖注入时的一致性。

3. **模板方法模式**：
   Spring 的 JdbcTemplate 和 HibernateTemplate 等模板类使用了模板方法模式。例如，JdbcTemplate 提供了一系列执行数据库操作的方法，其中包括了模板方法
   execute()
   ，而具体的数据库操作在其中由回调方法来实现。

4. **观察者模式**：
   Spring 的事件（Event）机制使用了观察者模式。ApplicationContext 可以发布事件，而监听器（ApplicationListener）可以订阅这些事件，并在事件发生时做出相应的处理。

5. **代理模式**：
   Spring AOP 使用了代理模式来实现面向切面编程。在 Spring AOP 中，目标对象被代理对象包装，代理对象拦截目标对象的方法调用，并在方法执行前后执行额外的逻辑。

### IOC 容器对 Bean 的生命周期

在 Spring 中，当一个 bean 被 IoC 容器创建并初始化之后，可能会涉及到以下几种方法的调用：

1. **构造函数（Constructor）**：
   首先，IoC 容器会调用 bean 类的构造函数来创建 bean 的实例。构造函数用于初始化 bean 实例的状态，并确保 bean
   的一致性和正确性。构造函数的调用发生在 bean 实例化的阶段。

2. **属性设置（Properties Setting）**：
   在 bean 实例化后，IoC 容器会注入 bean 的属性值。这包括通过 setter 方法注入属性值，或者通过字段直接注入属性值（使用
   `@Autowired` 或 `@Inject` 注解）。属性设置的调用发生在 bean
   实例化之后，属性注入之前。

3. **自定义初始化方法（Custom Initialization Method）**：
   在属性设置完成之后，可以定义一个自定义的初始化方法，用于执行一些额外的初始化操作。这个初始化方法通常使用
   `@PostConstruct` 注解标记，表示在属性注入完成后立即调用。在这个初始化方法中，可以执行一些与 bean
   相关的初始化逻辑，例如数据加载、资源初始化等。

4. **BeanPostProcessor 的前置处理（Before Initialization）**：
   在调用自定义初始化方法之前，IoC 容器会调用所有注册的 BeanPostProcessor 的 `postProcessBeforeInitialization()`
   方法。BeanPostProcessor 是 Spring
   容器的扩展点，允许开发者在 bean
   初始化前后做一些额外的处理。在 `postProcessBeforeInitialization()` 方法中，开发者可以对 bean 进行修改、增强或者验证等操作。

5. **自定义初始化方法的调用**：
   IoC 容器调用 bean 的自定义初始化方法，例如使用 `@PostConstruct` 注解标记的方法。在这个方法中，可以执行一些与 bean
   相关的初始化逻辑。

6. **BeanPostProcessor 的后置处理（After Initialization）**：
   在调用自定义初始化方法之后，IoC 容器会再次调用所有注册的 BeanPostProcessor 的 `postProcessAfterInitialization()`
   方法。在这个方法中，开发者可以对 bean
   进行进一步的修改、增强或者验证等操作。

7. **使用 bean**：
   在初始化完成后，bean 就处于可用状态，可以被其他 bean 或者应用程序的其他部分使用。在应用程序运行期间，可以使用容器的获取方法获取
   bean 实例，并使用它们提供的功能。

### spring中各种后置处理器

BeanFactoryPostProcessor 和 BeanPostProcessor 是 Spring 框架中两个重要的接口，它们在 Spring 容器初始化和实例化 Bean
的过程中起着不同的作用。

1. **BeanFactoryPostProcessor：**
    - BeanFactoryPostProcessor 是在 Spring 容器实例化 Bean 之前执行的一种扩展机制。
    - 它允许在容器实例化 Bean 之前修改 Bean 的定义（例如，修改属性值、添加新的属性等）。
    - BeanFactoryPostProcessor 接口有一个方法：`void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)`
      ，在这个方法中，你可以获取和修改 Bean 的定义信息。

2. **BeanPostProcessor：**
    - BeanPostProcessor 是在 Spring 容器实例化 Bean 后，在调用 Bean 的初始化方法前后执行的一种扩展机制。
    - 它允许在每个 Bean 实例化后对 Bean 进行一些额外的处理，例如，对 Bean 进行代理、添加监听器等。
    - BeanPostProcessor 接口有两个方法：`Object postProcessBeforeInitialization(Object bean, String beanName)`
      和 `Object postProcessAfterInitialization(Object bean, String beanName)`，分别在 Bean 初始化方法执行前后被调用。

区分它们的关键在于执行时机和作用对象：

- BeanFactoryPostProcessor 在 Spring 容器实例化 Bean 之前执行，作用于 Bean 的定义；
- BeanPostProcessor 在 Spring 容器实例化 Bean 后，在初始化方法执行前后执行，作用于每个具体的 Bean 实例。



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



## 问题

### 什么是循环依赖,spring是如何解决循环依赖问题的?

循环依赖是指一个或多个类出现互相依赖的关系而导致的问题，spring 通过三级缓存解决循环依赖的问题。
循环依赖有三种不同的形式出现：
1. 属性注入 - 不再推荐的注入方式，缺乏可见性和控制力，让替换变得困难，且无法注入一个final对象，但是spring可以解决这种形式的循环依赖
2. 方法注入 - 同样也无法注入不可变对象，注入的对象还可能被修改，spring可以解决这种形式的循环依赖
3. 构造器注入 - *这种方式是无法解决循环依赖的，所以如果存在循环依赖，又使用构造器来实现依赖注入，那么容器将无法初始化

[spring为什么使用三级缓存而不是两级](spring为什么使用三级缓存而不是两级.mhtml) 这篇文章写得不错
