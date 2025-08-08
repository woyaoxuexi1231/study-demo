# 概念

[spring实战(第五版) 要梯子](https://potoyang.gitbook.io/spring-in-action-v5)



## 简要介绍一下spring各个模块

Spring 框架是一个模块化的框架，拥有多个模块，每个模块都专注于不同的功能领域。以下是 Spring 框架中一些主要的模块：

1. **Spring Core Container**：
   
    - **IoC Container（控制反转容器）**：Spring 的 IoC 容器是 Spring 框架的核心，负责管理应用程序中的对象（Bean）的生命周期、依赖关系、配置和注入。它包括 BeanFactory 和 ApplicationContext 两个主要实现。
        - **BeanFactory**
            - `BeanFactory` 是 Spring 框架的核心接口之一，它负责管理应用程序中的对象（Bean）的生命周期、依赖关系、配置和注入。
            - `BeanFactory` 通过将应用程序的配置信息加载到容器中，并使用这些信息实例化、配置和组装 Bean 对象，从而实现了控制反转（IoC）和依赖注入（DI）。
            - `BeanFactory` 提供了许多实现类，其中最常用的是 `DefaultListableBeanFactory` 和 `XmlBeanFactory`。
        - **ApplicationContext**
            - `ApplicationContext` 是 `BeanFactory` 的子接口，它是 Spring 框架中更高级的容器，提供了更多的功能和特性。
            - `ApplicationContext` 在 BeanFactory 的基础上，添加了对国际化、事件传播、资源加载、AOP 等方面的支持。
            - `ApplicationContext` 实现类包括 `ClassPathXmlApplicationContext`、`FileSystemXmlApplicationContext`、`AnnotationConfigApplicationContext` 等。
        - 这两个部分是 Spring Core Container 的核心组件，它们提供了 Spring 框架中最基本的功能，例如 Bean 的创建、依赖注入、配置管理、生命周期管理等。通过这些功能，Spring Core Container 实现了松耦合、模块化和可测试性，使得应用程序的开发和维护变得更加简单和灵活。
    - **Dependency Injection（依赖注入）**：Spring 使用依赖注入来管理对象之间的依赖关系，使得对象之间的耦合度降低，更易于测试和维护。
    
2. **Spring AOP（面向切面编程）**：
    - **Aspect（切面）**：切面是一种模块化的方式，用于横向切割关注点（cross-cutting concerns），例如事务管理、日志记录、安全等。
    - **Join point（连接点）**：连接点是在应用程序执行期间可以插入切面的点，例如方法的调用、异常的处理等。
    - **Advice（通知）**：通知是切面在连接点上执行的动作，例如在方法执行前后、抛出异常时等。 前置通知（Before advice）、后置通知（After returning advice）、异常通知（After throwing advice）、最终通知（After finally advice）、环绕通知（Around advice）。
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
    **@Aspect** 注解标记了这个类为一个切面。  
   
    **@Before** 注解标记了一个前置通知，指定了切入点表达式 "execution(* com.example.service.UserService.*(..))"，表示所有 UserService 类中的方法调用作为连接点。  
   
   **beforeAdvice()** 方法中，我们定义了在方法执行之前输出日志的逻辑。
   
3. **Spring DAO（数据访问）**：
   Spring DAO 模块是 Spring 框架的一个重要部分，专注于简化数据访问层的开发。它提供了许多实用的功能和技术，使得与数据库交互更加简单、高效和可维护。下面是 Spring DAO 模块的一些主要特点和功能：
   
    1. **JDBC 抽象层**：
       Spring DAO 提供了对 JDBC 的抽象，简化了 JDBC 编程的复杂性。它包括了一系列的 JDBC 模板类（例如JdbcTemplate、NamedParameterJdbcTemplate），这些模板类提供了许多便捷的方法来执行 SQL 查询、更新和批处理操作，同时封装了异常处理和资源释放等操作，减少了样板式代码的编写。
    2. **ORM 整合**：
       Spring DAO 提供了与主流的 ORM 框架（例如 Hibernate、JPA）的集成支持，使得在 Spring 应用中使用 ORM 技术变得更加容易。通过 Spring DAO，可以轻松地将 ORM 框架的功能与 Spring 的事务管理、依赖注入等特性无缝整合，从而实现更加灵活和可维护的数据访问层。
    3. **声明式事务管理**：
       Spring DAO 提供了声明式事务管理的支持，使得在 Spring 应用中管理事务变得更加简单和灵活。通过声明式事务管理，开发者可以使用注解或 XML 配置来定义事务的边界和传播行为，而无需手动编写事务管理代码。
    4. **数据访问异常处理**：
       Spring DAO 提供了一套一致的异常体系用于处理数据访问中的异常，使得在应用程序中统一处理数据库相关的异常变得更加方便和可靠。
    5. **数据访问支持类**：
       Spring DAO 提供了许多支持类和工具类，用于简化数据访问层的开发。例如，它提供了 RowMapper 接口用于将查询结果集映射到 Java 对象，提供了 SQL 参数的封装类 SqlParameterSource，提供了 JdbcOperations 接口用于执行 JDBC 操作等。
   
   总的来说，Spring DAO 模块通过提供一系列的抽象、工具和技术，使得在 Spring 应用中进行数据访问变得更加简单、灵活和高效。无论是直接使用 JDBC 进行操作，还是通过集成的 ORM 框架进行持久化操作，都可以得到很好的支持和便利。
   
4. **Spring MVC（模型-视图-控制器）**：
   
    - **DispatcherServlet（调度器 Servlet）**：Spring MVC 的核心组件，负责接收 HTTP 请求并将其分派到相应的处理器（Controller）上进行处理。
    - **HandlerMapping（处理器映射器）**：负责将请求映射到相应的处理器上。
    - **Controller（控制器）**：处理 HTTP 请求，执行业务逻辑，并返回相应的视图。
    - **ViewResolver（视图解析器）**：将逻辑视图名称解析为实际的视图对象。
    
5. **Spring Security（安全框架）**：Spring Security 是一个功能强大的安全框架，用于保护应用程序的安全性，包括身份验证、授权、会话管理、加密等功能。

6. **Spring Boot（快速应用开发）**：Spring Boot 是 Spring 的一个子项目，旨在简化 Spring 应用程序的开发和部署。它提供了自动配置、起步依赖、内嵌容器等特性，使得开发者可以更加快速地构建独立的、生产级别的应用程序。
   
7. **Spring Data（数据访问）**：Spring Data 是一个用于简化数据访问的项目，它为不同的数据存储提供了统一的数据访问抽象，并提供了 Repository 接口用于简化数据访问层的开发。
   
8. **Spring Integration（集成框架）**：Spring Integration 是一个用于企业集成的框架，提供了丰富的集成模式和组件，用于处理消息、文件、WebService 等各种集成场景。



## spring用到了哪些设计模式

Spring 框架在其设计和实现中运用了多种设计模式，其中一些常见的设计模式包括但不限于：

1. **工厂模式**：

   Spring 使用工厂模式来创建和管理对象的生命周期。例如，BeanFactory 和 ApplicationContext 是 Spring 中常用的工厂类，它们负责创建和管理 bean 对象。

2. **单例模式**：

   Spring 中的 bean 默认是单例的，即每个 bean 在容器中只有一个实例。这样可以节省资源，并且保证了依赖注入时的一致性。

3. **模板方法模式**：

   Spring 的 JdbcTemplate 和 HibernateTemplate 等模板类使用了模板方法模式。例如，JdbcTemplate 提供了一系列执行数据库操作的方法，其中包括了模板方法 execute()，而具体的数据库操作在其中由回调方法来实现。

4. **观察者模式**：

   Spring 的事件（Event）机制使用了观察者模式。ApplicationContext 可以发布事件，而监听器（ApplicationListener）可以订阅这些事件，并在事件发生时做出相应的处理。

5. **代理模式**：

   Spring AOP 使用了代理模式来实现面向切面编程。在 Spring AOP 中，目标对象被代理对象包装，代理对象拦截目标对象的方法调用，并在方法执行前后执行额外的逻辑。

## IOC 容器对 Bean 的生命周期

在 Spring 中，当一个 bean 被 IoC 容器创建并初始化之后，可能会涉及到以下几种方法的调用：

1. **构造函数（Constructor）**：
   首先，IoC 容器会调用 bean 类的构造函数来创建 bean 的实例。构造函数用于初始化 bean 实例的状态，并确保 bean 的一致性和正确性。构造函数的调用发生在 bean 实例化的阶段。
   
2. **属性设置（Properties Setting）**：
   在 bean 实例化后，IoC 容器会注入 bean 的属性值。这包括通过 setter 方法注入属性值，或者通过字段直接注入属性值（使用
   `@Autowired` 或 `@Inject` 注解）。属性设置的调用发生在 bean 实例化之后，属性注入之前。
   
3. **自定义初始化方法（Custom Initialization Method）**：
   在属性设置完成之后，可以定义一个自定义的初始化方法，用于执行一些额外的初始化操作。这个初始化方法通常使用 `@PostConstruct` 注解标记，表示在属性注入完成后立即调用。在这个初始化方法中，可以执行一些与 bean 相关的初始化逻辑，例如数据加载、资源初始化等。
   
4. **BeanPostProcessor 的前置处理（Before Initialization）**：
   在调用自定义初始化方法之前，IoC 容器会调用所有注册的 BeanPostProcessor 的 `postProcessBeforeInitialization()`
   方法。BeanPostProcessor 是 Spring 容器的扩展点，允许开发者在 bean 初始化前后做一些额外的处理。在 `postProcessBeforeInitialization()` 方法中，开发者可以对 bean 进行修改、增强或者验证等操作。
   
5. **自定义初始化方法的调用**：
   IoC 容器调用 bean 的自定义初始化方法，例如使用 `@PostConstruct` 注解标记的方法。在这个方法中，可以执行一些与 bean
   相关的初始化逻辑。

6. **BeanPostProcessor 的后置处理（After Initialization）**：
   在调用自定义初始化方法之后，IoC 容器会再次调用所有注册的 BeanPostProcessor 的 `postProcessAfterInitialization()` 方法。在这个方法中，开发者可以对 bean 进行进一步的修改、增强或者验证等操作。
   
7. **使用 bean**：
   在初始化完成后，bean 就处于可用状态，可以被其他 bean 或者应用程序的其他部分使用。在应用程序运行期间，可以使用容器的获取方法获取 bean 实例，并使用它们提供的功能。

## spring中各种后置处理器

BeanFactoryPostProcessor 和 BeanPostProcessor 是 Spring 框架中两个重要的接口，它们在 Spring 容器初始化和实例化 Bean
的过程中起着不同的作用。

1. **BeanFactoryPostProcessor：**
    - BeanFactoryPostProcessor 是在 Spring 容器实例化 Bean 之前执行的一种扩展机制。
    - 它允许在容器实例化 Bean 之前修改 Bean 的定义（例如，修改属性值、添加新的属性等）。
    - BeanFactoryPostProcessor 接口有一个方法：`void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)` ，在这个方法中，你可以获取和修改 Bean 的定义信息。
    
2. **BeanPostProcessor：**
    - BeanPostProcessor 是在 Spring 容器实例化 Bean 后，在调用 Bean 的初始化方法前后执行的一种扩展机制。
    - 它允许在每个 Bean 实例化后对 Bean 进行一些额外的处理，例如，对 Bean 进行代理、添加监听器等。
    - BeanPostProcessor 接口有两个方法：`Object postProcessBeforeInitialization(Object bean, String beanName)` 和 `Object postProcessAfterInitialization(Object bean, String beanName)`，分别在 Bean 初始化方法执行前后被调用。

区分它们的关键在于执行时机和作用对象：

- BeanFactoryPostProcessor 在 Spring 容器实例化 Bean 之前执行，作用于 Bean 的定义；
- BeanPostProcessor 在 Spring 容器实例化 Bean 后，在初始化方法执行前后执行，作用于每个具体的 Bean 实例。



# Spring IOC (控制反转) 详解

## 基本概念

IOC (Inversion of Control，控制反转) 是 Spring 框架的核心思想之一，也被称为依赖注入(DI, Dependency Injection)。

### 传统编程模式 vs IOC模式

**传统模式**：
- 对象自己创建和管理依赖对象
- 代码耦合度高
- 难以测试和维护

**IOC模式**：
- 依赖对象的创建和管理交给外部容器(Spring容器)
- 代码只需声明依赖关系
- 耦合度低，易于测试和维护

## IOC 容器

Spring IOC 容器是 Spring 框架的核心，主要负责：

1. 实例化应用程序中的对象
2. 配置这些对象
3. 管理这些对象的依赖关系
4. 组装对象之间的关系

### 主要接口

1. **BeanFactory**：基础容器接口，提供基本功能
2. **ApplicationContext**：BeanFactory 的子接口，提供更多企业级功能

## 依赖注入(DI)的三种方式

1. **构造器注入**：
   ```java
   public class UserService {
       private UserRepository userRepository;
       
       public UserService(UserRepository userRepository) {
           this.userRepository = userRepository;
       }
   }
   ```

2. **Setter方法注入**：
   ```java
   public class UserService {
       private UserRepository userRepository;
       
       public void setUserRepository(UserRepository userRepository) {
           this.userRepository = userRepository;
       }
   }
   ```

3. **字段注入(不推荐)**：
   ```java
   public class UserService {
       @Autowired
       private UserRepository userRepository;
   }
   ```

## IOC 的优势

1. **降低耦合度**：对象间依赖关系由容器管理
2. **提高可测试性**：易于进行单元测试
3. **提高可维护性**：配置集中管理
4. **灵活性**：通过配置即可改变实现
5. **生命周期管理**：容器管理对象的完整生命周期

## Spring Bean 的作用域

Spring 管理的对象称为 Bean，支持以下作用域：

1. **singleton**（默认）：每个容器一个实例
2. **prototype**：每次请求都创建新实例
3. **request**：每个 HTTP 请求一个实例
4. **session**：每个 HTTP 会话一个实例
5. **application**：每个 ServletContext 一个实例
6. **websocket**：每个 WebSocket 会话一个实例

## 配置方式

Spring IOC 可以通过以下方式配置：

1. **XML 配置**：
   ```xml
   <bean id="userService" class="com.example.UserService">
       <property name="userRepository" ref="userRepository"/>
   </bean>
   ```

2. **Java 注解**：
   
   ```java
   @Service
   public class UserService {
       @Autowired
       private UserRepository userRepository;
   }
   ```
   
3. **Java 配置类**：
   ```java
   @Configuration
   public class AppConfig {
       @Bean
       public UserService userService() {
           return new UserService(userRepository());
       }
       
       @Bean
       public UserRepository userRepository() {
           return new JdbcUserRepository();
       }
   }
   ```

## Spring IOC 代码对比示例

下面我将通过代码对比展示传统编程方式与Spring IOC方式的区别。

### 传统方式 vs Spring IOC方式

场景：UserService 依赖 UserRepository

##### 传统方式 (手动管理依赖)

```java
// UserRepository接口
public interface UserRepository {
    void save(User user);
}

// UserRepository实现
public class JdbcUserRepository implements UserRepository {
    @Override
    public void save(User user) {
        System.out.println("Saving user to database: " + user);
    }
}

// UserService
public class UserService {
    private UserRepository userRepository;
    
    // 手动创建依赖
    public UserService() {
        this.userRepository = new JdbcUserRepository(); // 紧耦合
    }
    
    public void register(User user) {
        userRepository.save(user);
    }
}

// 使用
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService(); // 需要自己创建对象
        userService.register(new User("Alice"));
    }
}
```

##### Spring IOC方式 (容器管理依赖)

```java
// 同样的接口和实现
public interface UserRepository {
    void save(User user);
}

public class JdbcUserRepository implements UserRepository {
    @Override
    public void save(User user) {
        System.out.println("Saving user to database: " + user);
    }
}

// UserService
@Service // 标记为Spring管理的Bean
public class UserService {
    private final UserRepository userRepository;
    
    // 通过构造器注入依赖
    @Autowired // 可省略(Spring 4.3+会自动注入)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; // 松耦合
    }
    
    public void register(User user) {
        userRepository.save(user);
    }
}

// 配置类
@Configuration
@ComponentScan // 扫描当前包及其子包
public class AppConfig {
}

// 使用
public class Main {
    public static void main(String[] args) {
        // 由Spring容器管理对象创建和依赖注入
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        userService.register(new User("Alice"));
    }
}
```



### 关键区别总结

| 方面     | 传统方式         | Spring IOC方式 |
| -------- | ---------------- | -------------- |
| 对象创建 | 手动new创建      | 容器自动创建   |
| 依赖管理 | 类内部硬编码依赖 | 外部注入依赖   |
| 耦合度   | 高耦合           | 低耦合         |
| 可测试性 | 难以mock依赖     | 易于mock测试   |
| 灵活性   | 修改实现需改代码 | 只需修改配置   |
| 生命周期 | 手动管理         | 容器管理       |



# Spring Aop 

## 基本概念

AOP (Aspect-Oriented Programming，面向切面编程) 是 Spring 框架的另一个核心特性，它是对 OOP (面向对象编程) 的补充，用于处理横切关注点(cross-cutting concerns)。

### 核心思想

将应用中分散在各处的相同功能(如日志、事务、安全等)提取出来，形成独立的"切面"，从而避免代码重复和业务逻辑混杂。



## aop 的各种概念

AOP（Aspect-Oriented Programming，面向切面编程）是一种软件开发方法，旨在通过将横切关注点（cross-cutting concerns）从核心业务逻辑中分离出来，以提高代码的模块化性、可维护性和可重用性。以下是 AOP 中常见的概念：

1. **切面（Aspect）：**

   切面是一个模块化单元，它横切应用程序的多个类。它封装了与特定横切关注点（如日志、事务管理、安全性等）相关的行为并可以在程序中的多个位置重复使用。切面可以包含通知以及切点。  

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

   匹配连接点的表达式，确定哪些连接点会应用通知。

   切点是指一组连接点的集合，允许开发者定义在哪些连接点应用通知。通知只在与切点匹配的连接点上被执行。
   JoinPoint 是我们逻辑上的一个点，而 Pointcut 在代码层面帮我们描述 JoinPoint 且 Pointcut可以描述由一堆 JoinPoint 的集合, SpringAop 中的 @PointCut 注解

5. **引入（Introduction）：**

   引入允许向现有类添加新方法或属性。在 AOP 中，引入允许切面向现有的类添加新功能，而无需修改它们的代码。

6. **织入（Weaving）：**

   织入是将切面与应用程序的目标对象连接起来并创建新的代理对象的过程。

   织入可以在编译时、加载时或运行时完成。在 Spring 中，AOP 通常通过动态代理或者字节码增强来实现织入。

7. **Weaver（织入器）：**
   Weaver 是 AOP 中负责将切面与目标对象连接起来并创建代理对象的组件。它负责将切面的通知逻辑应用到目标对象的连接点上，从而实现横切关注点的织入。

   Weaver 可以在编译时、加载时或者运行时进行织入操作。常见的织入方式包括静态代理、动态代理和字节码增强。

   在 Spring AOP中，Weaver 负责将切面织入到 Spring 容器管理的 Bean 上，以创建代理对象来实现切面功能。

8. **Target Object（目标对象）：**

   Target Object 是指在 AOP 中被切面所影响或增强的原始对象。它是应用程序中实际执行业务逻辑的对象。

   通常，切面中的通知会围绕着目标对象的连接点来执行相应的操作，例如在方法调用前后添加日志、在方法执行时处理事务等。目标对象可以是任何普通的 Java 对象，包括 POJO（Plain Old Java Object）、Spring 管理的 Bean 等。

这些概念共同构成了 AOP 的核心，通过 AOP，开发者可以将横切关注点与核心业务逻辑分离，提高了代码的可维护性和可重用性。

## spring aop

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

## spring aop 一些核心类和接口

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

## 切入点表达式

Spring AOP 使用 AspectJ 的切入点表达式语言，常见表达式：

1. `execution([修饰符] 返回类型 [类名].方法名(参数) [异常])` - 匹配方法执行
   - `execution(* com.example.service.*.*(..))` - 匹配service包下所有类的所有方法
   - `execution(public * *(..))` - 匹配所有public方法
2. `within(类型表达式)` - 匹配特定类型内的方法
   - `within(com.example.service.*)` - 匹配service包下所有类
3. `this(类型)` - 匹配代理对象是指定类型的bean
4. `target(类型)` - 匹配目标对象是指定类型的bean
5. `args(参数类型)` - 匹配参数类型



## AbstractAutoProxyCreator

 `AbstractAutoProxyCreator` 是 **Spring AOP** 中非常核心的一个抽象类，位于 `org.springframework.aop.framework.autoproxy` 包下。
 它是 Spring **自动代理机制**（Auto Proxying）的基石，核心职责是：

> 根据容器中的 Bean 的定义和配置（如 Advisor、切面等），在 Bean 初始化时自动给 Bean 生成代理对象（Proxy）。

------

### 📌 一、`AbstractAutoProxyCreator` 的作用

- 是所有自动代理创建器（Auto Proxy Creator）的抽象父类。
- 实现了 `BeanPostProcessor`，在 Bean 初始化前后插入代理逻辑。
- 根据 `Advisor`、`Interceptor`、`Pointcut` 等，决定哪些 Bean 需要生成代理。
- 是 Spring AOP 和 Spring Transaction、Spring Security 等功能的核心支撑。

------

### 📌 二、几个最常用的重要子类及作用

下面是几个常用且重要的直接或间接子类：

------

#### 1️⃣ `InfrastructureAdvisorAutoProxyCreator`

**作用**：

- 最基础的自动代理创建器，所有基于 `Advisor` 的自动代理逻辑都基于它。
- 检测容器中所有的 `Advisor`（比如 `PointcutAdvisor`）来匹配 Bean，生成代理。

**应用场景**：

- Spring AOP 的基础实现一般就依赖它，比如 `<aop:config>` 里的 `AspectJAutoProxyBeanDefinitionParser` 最终会注册它或其子类。

------

#### 2️⃣ `AspectJAwareAdvisorAutoProxyCreator`

**作用**：

- 在 `InfrastructureAdvisorAutoProxyCreator` 的基础上，加入了对 `@Aspect` 的支持。
- 能识别基于 `@Aspect` 注解的切面，并将其转化为 `Advisor`。

**应用场景**：

- 配合 `@Aspect` 注解使用时，必须使用该类或其子类。

------

#### 3️⃣ `AnnotationAwareAspectJAutoProxyCreator`

**作用**：
 ✅ **最常用！**

- 继承 `AspectJAwareAdvisorAutoProxyCreator`，支持基于注解的 AspectJ 风格 AOP。
- 是 `<aop:aspectj-autoproxy>` 和 `@EnableAspectJAutoProxy` 的默认实现。
- 解析 `@Aspect` 注解，构建 Advisor，生成代理。

**应用场景**：

- `@Aspect` + `@EnableAspectJAutoProxy` 时必用。
- Spring Boot 项目里如果启用了基于注解的 AOP，实际就是它在背后工作。



`AnnotationAwareAspectJAutoProxyCreator` 是 Spring 框架中的一个类，用于支持基于注解的 AspectJ 切面编程。它是自动代理创建器的一种，特别负责识别带有 AspectJ 注解 (`@Aspect`) 的类，并为它们自动创建代理。这个过程是 AOP (面向切面编程) 的核心组成部分，允许开发者定义切面（aspects）、通知（advises）、切点（pointcuts）等，以便在不修改源代码的情况下增加额外的行为（比如日志记录、事务管理等）。

具体来说，`AnnotationAwareAspectJAutoProxyCreator` 执行以下关键任务：

1. **检测`@Aspect`注解**：它会扫描应用上下文中所有的 Bean，寻找带有 `@Aspect` 注解的类，这些类定义了切面的规则和逻辑。

2. **创建代理**：对于那些与切点表达式匹配的 Bean，`AnnotationAwareAspectJAutoProxyCreator` 会为它们创建代理。这些代理在目标方法执行前后可以执行额外的逻辑，如前置通知（Before advice）、后置通知（After returning
   advice）、异常通知（After throwing advice）等。

3. **管理通知和切点**：它还负责处理这些切面中定义的通知（advice）和切点（pointcut）逻辑，确保在正确的时间、正确的地点执行预定的操作。

`AnnotationAwareAspectJAutoProxyCreator` 是 Spring AOP 功能的关键组件之一，使得在 Spring 应用中实现面向切面编程变得更加简单和直接。通过使用 AspectJ 的注解，开发者可以非常灵活地定义横切关注点，而无需深入了解复杂的 AOP 概念或代理机制。

这个类属于 Spring AOP 和 AspectJ 支持的一部分，通常是通过配置（如使用 Java 配置或 XML 配置）自动注册和激活的，而开发者主要关注于定义切面和通知逻辑。



------

#### 4️⃣ `DefaultAdvisorAutoProxyCreator`

**作用**：

- 直接继承 `AbstractAdvisorAutoProxyCreator`（和 `InfrastructureAdvisorAutoProxyCreator` 很像）。
- 提供了默认的 Advisor 自动代理功能。
- 在一些老的 XML 配置里 `<aop:advisor>` 时会注册它。

------

#### 5️⃣ `BeanNameAutoProxyCreator`

**作用**：

- 比较简单的实现类。
- 根据 Bean 的名字匹配来创建代理（可指定需要代理哪些 Bean 名称）。
- 主要用于基于 Bean 名称的静态代理，不需要使用 `Advisor`、`Pointcut`。

**应用场景**：

- 在一些老项目里，用户可以只配置需要代理的 Bean 名称，然后指定拦截器即可。

------

### 📌 三、常见的实际使用关系（简化层次）

```
AbstractAutoProxyCreator
   ├─ AbstractAdvisorAutoProxyCreator
        ├─ InfrastructureAdvisorAutoProxyCreator
             ├─ AspectJAwareAdvisorAutoProxyCreator
                  ├─ AnnotationAwareAspectJAutoProxyCreator
```

------

### 📌 四、实际触发点

- 使用 `<aop:aspectj-autoproxy/>` ➜ 注册 `AnnotationAwareAspectJAutoProxyCreator`
- 使用 `@EnableAspectJAutoProxy` ➜ 注册 `AnnotationAwareAspectJAutoProxyCreator`
- 使用 `<aop:config>` + `<aop:advisor>` ➜ 注册 `DefaultAdvisorAutoProxyCreator` 或 `InfrastructureAdvisorAutoProxyCreator`

------

### ✅ 总结

| 类名                                     | 作用                          | 典型用法                  |
| ---------------------------------------- | ----------------------------- | ------------------------- |
| `InfrastructureAdvisorAutoProxyCreator`  | 基础 Advisor 自动代理         | AOP 基础                  |
| `AspectJAwareAdvisorAutoProxyCreator`    | 支持 `@Aspect` 注解           | AspectJ AOP               |
| `AnnotationAwareAspectJAutoProxyCreator` | 最常用，AspectJ 注解自动代理  | `@EnableAspectJAutoProxy` |
| `DefaultAdvisorAutoProxyCreator`         | XML 下默认的 Advisor 自动代理 | `<aop:config>`            |
| `BeanNameAutoProxyCreator`               | 根据 Bean 名字匹配            | 静态名称代理              |

------

**一句话记住：**

> Spring 的 AOP 自动代理就是靠 `AbstractAutoProxyCreator` 体系在 `BeanPostProcessor` 中在 Bean 实例化后套一层代理完成的。

------



# spring 循环依赖问题

循环依赖是指一个或多个类出现互相依赖的关系而导致的问题，spring 通过三级缓存解决循环依赖的问题。
循环依赖有三种不同的形式出现：

1. 属性注入 - 不再推荐的注入方式，缺乏可见性和控制力，让替换变得困难，且无法注入一个final对象，但是spring可以解决这种形式的循环依赖
2. 方法注入 - 同样也无法注入不可变对象，注入的对象还可能被修改，spring可以解决这种形式的循环依赖
3. 构造器注入 - *这种方式是无法解决循环依赖的，所以如果存在循环依赖，又使用构造器来实现依赖注入，那么容器将无法初始化

[spring为什么使用三级缓存而不是两级](spring为什么使用三级缓存而不是两级.mhtml) 这篇文章写得不错



# Spring MVC

## springmvc 框架简介

Spring MVC 是 Spring 框架的一个 Web 开发框架，用于构建 Web 应用程序和 RESTful 服务。它采用了经典的 MVC（模型-视图-控制器）设计模式，将一个 Web 应用程序分成三个核心部分：模型（Model）、视图（View）和控制器（Controller），以提高代码的模块化程度、可维护性和可测试性。
下面是 Spring MVC 的三个核心组件及其作用的简要介绍：

1. **模型（Model）**：

   模型代表应用程序的数据和业务逻辑。在 Spring MVC 中，模型通常是一个普通的 Java 对象（POJO），用于封装和处理数据。模型对象通常存储在请求作用域中，可以通过请求处理流程来获取和操作。模型与视图之间是独立的，视图不会直接修改模型数据。

2. **视图（View）**：

   视图负责将模型数据呈现给用户，并处理用户的交互。在 Spring MVC 中，视图通常是一个 JSP 页面、Thymeleaf 模板或者是 JSON/XML 等数据格式。视图负责将模型数据渲染成用户可见的界面，以便用户进行交互操作。在处理完请求之后，控制器会将模型数据传递给适当的视图进行呈现。

3. **控制器（Controller）**：

   控制器负责处理用户请求，并协调模型和视图之间的交互。在 Spring MVC 中，控制器通常是一个带有 `@Controller` 注解的 Java 类，它使用 `@RequestMapping` 注解来映射请求 URL 和请求方法。控制器接收用户请求后，会调用适当的业务逻辑处理方法，然后将模型数据传递给视图进行呈现。

## springmvc 的各个模块介绍

Spring MVC 框架提供了一套灵活的处理器映射机制、视图解析机制和异常处理机制，使得开发者能够更加方便地构建 Web 应用程序。

它还提供了丰富的标签库和注解，用于简化表单处理、数据绑定、数据验证等操作。

另外，Spring MVC 框架还支持 RESTful 风格的 Web 服务开发，通过 `@RestController` 注解可以很方便地创建 RESTful 服务。

总的来说，Spring MVC 是一个功能强大、灵活性高、易于使用的 Web 开发框架，广泛应用于构建各种类型的 Web 应用程序和 RESTful 服务。

### DispatcherServlet（调度器 Servlet）

Spring MVC 的核心组件，负责接收 HTTP 请求并将其分派到相应的处理器（Controller）上进行处理。
`DispatcherServlet`（调度器 Servlet）是 Spring MVC 框架的核心组件之一，它是一个特殊的 Servlet，负责接收客户端的 HTTP
请求，并将请求分派到相应的处理器（Controller）进行处理。

DispatcherServlet 在整个 Spring MVC 请求处理流程中起着非常重要的作用。

以下是 DispatcherServlet 的主要功能和特点：

- **接收请求**：
  
  DispatcherServlet 作为应用程序的前端控制器（Front Controller），负责接收所有客户端的 HTTP 请求。它监听一个或多个 URL 地址，并拦截所有与这些 URL 地址匹配的请求。
  
- **请求处理流程**：
  
  一旦接收到请求，DispatcherServlet 将根据配置的处理器映射（Handler Mapping）来确定该请求应该由哪个处理器来处理。然后，它将请求委派给相应的处理器（Controller）进行处理，并等待处理器的执行结果。
  
- **处理器执行**：
  
  处理器（Controller）是实际处理请求的组件，它执行业务逻辑并生成模型数据。DispatcherServlet 调用处理器的方法，并将请求的上下文信息传递给处理器。处理器执行完毕后，通常会返回一个逻辑视图名或者一个视图对象。
  
- **视图解析**：
  DispatcherServlet 将处理器返回的逻辑视图名（或者视图对象）解析成实际的视图对象。它使用视图解析器（View Resolver）来完成这个工作，视图解析器会根据配置的规则来将逻辑视图名解析成实际的视图对象（如 JSP 页面、Thymeleaf 模板等）。
  
- **视图呈现**：
  一旦确定了视图对象，DispatcherServlet 将调用视图对象的渲染方法来生成最终的响应内容。视图对象通常负责将模型数据填充到视图中，并将渲染结果发送给客户端。
  
- **异常处理**：
  在整个请求处理流程中，如果出现异常，DispatcherServlet 会调用配置的异常处理器（Exception Resolver）来处理异常。异常处理器负责捕获和处理异常，并生成合适的错误响应。
  
- **拦截器支持**：
  DispatcherServlet 支持拦截器（Interceptor）机制，允许开发者在请求处理的不同阶段进行预处理或后处理操作。拦截器可以在
  DispatcherServlet 的请求处理流程中插入自定义的逻辑。总的来说，DispatcherServlet 在 Spring MVC 框架中起着非常重要的作用，它负责整个请求处理流程的协调和控制。通过适当的配置，开发者可以定制 DispatcherServlet s的行为，以满足不同的需求和场景。

### HandlerMapping（处理器映射器）

`HandlerMapping`（处理器映射器）是 Spring MVC 框架中的一个重要组件，它负责将请求映射到对应的处理器（Controller）上进行处理。
在Spring MVC 中，处理器映射器起着路由的作用，根据请求的URL 和其他条件来确定哪个处理器将处理该请求。
Spring MVC 提供了多种实现了 HandlerMapping 接口的默认处理器映射器，每种映射器都有不同的映射规则。
以下是一些常见的HandlerMapping 实现：

- **BeanNameUrlHandlerMapping**：
  
  这是一个简单的映射器，根据请求 URL 的路径查找相应的处理器 Bean。例如，如果请求的 URL 路径是 `/user`，则会查找名为
  `userController` 的处理器 Bean。
  
- **DefaultAnnotationHandlerMapping**：
  
  这个映射器会扫描 Spring 应用程序上下文中所有标记了 `@Controller` 注解的 Bean，并根据 `@RequestMapping` 注解的配置来确定请求路径与处理器之间的映射关系。
  
- **RequestMappingHandlerMapping**：
  
  这是 Spring MVC 中最常用的映射器，它支持多种注解（如 `@RequestMapping`、`@GetMapping`、`@PostMapping` 等）来定义请求路径与处理器的映射关系。它会根据请求的 HTTP 方法和路径来匹配对应的处理器。
  
- **SimpleUrlHandlerMapping**：
  
  这个映射器允许开发者通过配置显式地指定 URL 与处理器之间的映射关系。可以根据请求的 URL 路径或者请求的请求头信息等来匹配处理器。
  
- **RequestMappingInfoHandlerMapping**：
  
  这是 Spring MVC 5.0 引入的新的映射器，它支持更灵活的请求映射规则，可以基于请求路径、请求参数、请求头、请求方法等多个条件来匹配处理器。
  这些映射器的选择取决于应用程序的需求和开发者的偏好。通常情况下，开发者不需要自己选择映射器，Spring MVC 框架会根据配置自动选择合适的映射器来处理请求。 HandlerMapping 的工作是在 Spring MVC 的请求处理流程中的第一个步骤，它确定了请求将由哪个处理器来处理。

### Controller（控制器）

处理 HTTP 请求，执行业务逻辑，并返回相应的视图。
在 Spring MVC 框架中，Controller（控制器）是用于处理客户端请求并生成响应的组件。
Controller 负责接收请求、执行业务逻辑，并返回相应的视图或数据给客户端。
Controller 在MVC（模型-视图-控制器）架构中扮演着控制请求和响应的角色。
以下是 Controller 的主要特点和功能：

- **请求映射**：
  Controller 使用 `@RequestMapping` 注解或其衍生注解（如 `@GetMapping`、`@PostMapping` 等）来指定处理特定 URL
  地址的请求。通过这些注解，开发者可以将请求映射到特定的处理方法上。

   ```java
  @Controller
  public class MyController {
  
      @GetMapping("/hello")
      public String hello() {
          return "hello";
      }
  }
   ```

- **处理请求**：
  Controller 中的处理方法（handler method）负责执行实际的业务逻辑，并生成模型数据。处理方法通常是一个公开的方法，用于处理特定类型的请求，可以接收请求参数、执行业务逻辑，并返回响应数据。

   ```java
  @Controller
  public class MyController {
  
      @GetMapping("/hello")
      public String hello(Model model) {
          model.addAttribute("message", "Hello, world!");
          return "hello";
      }
  }
   ```

- **数据绑定**：
  Controller 可以使用 `@RequestParam`、`@PathVariable` 等注解来绑定请求参数或路径变量到处理方法的参数上。这样，开发者可以方便地获取客户端发送的数据，并在处理方法中进行处理。

   ```java
  @Controller
  public class MyController {
  
      @GetMapping("/hello")
      public String hello(@RequestParam("name") String name, Model model) {
          model.addAttribute("message", "Hello, " + name + "!");
          return "hello";
      }
  }
   ```

- **返回视图**：
  处理方法通常会返回一个逻辑视图名（Logical View Name），DispatcherServlet 会根据视图解析器（View
  Resolver）将逻辑视图名解析成实际的视图对象，并将视图渲染成最终的响应结果返回给客户端。

   ```java
  @Controller
  public class MyController {
  
      @GetMapping("/hello")
      public String hello() {
          return "hello";
      }
  }
   ```

- **返回数据**：
  除了返回视图外，处理方法还可以直接返回数据对象（如 POJO、Map、List 等），Spring MVC 框架会自动将这些数据转换成合适的响应格式（如
  JSON、XML）并发送给客户端。

   ```java
  @Controller
  public class MyController {
  
      @GetMapping("/data")
      @ResponseBody
      public Map<String, String> data() {
          Map<String, String> data = new HashMap<>();
          data.put("message", "Hello, world!");
          return data;
      }
  }
   ```

总的来说，Controller 在 Spring MVC 框架中扮演着非常重要的角色，它负责处理客户端的请求、执行业务逻辑，并生成相应的视图或数据。通过合适的配置和编码，开发者可以实现灵活、可维护的 Web 应用程序。

### ViewResolver（视图解析器）

将逻辑视图名称解析为实际的视图对象 ViewResolver（视图解析器）是 Spring MVC 框架中的一个关键组件，它负责将逻辑视图名称（Logical ViewName）解析为实际的视图对象（View）。
在处理完请求后， Controller 方法通常会返回一个逻辑视图名称，例如 `"hello"`，而不是直接返回一个具体的视图对象。ViewResolver 就是负责将这个逻辑视图名称解析成实际的视图对象的组件。
下面是 ViewResolver 的主要工作流程：

1. **获取逻辑视图名称**
   当处理器方法执行完成后，它会返回一个逻辑视图名称，例如 `"hello"`。
2. **视图解析**：
   DispatcherServlet 将这个逻辑视图名称传递给 ViewResolver，ViewResolver 会根据配置的规则和策略来将逻辑视图名称解析成实际的视图对象。
3. **视图渲染**：
   一旦获取到实际的视图对象，DispatcherServlet 就会调用视图对象的 `render` 方法来渲染视图，并生成最终的响应结果。
   渲染的结果通常是一个 HTML 页面、一个 JSON 字符串或者一个 XML 文档，这取决于具体的视图对象。

Spring MVC 框架提供了多种不同类型的 ViewResolver 实现，常见的 ViewResolver 包括：

- **InternalResourceViewResolver**：

  这是 Spring MVC 默认的视图解析器，它将逻辑视图名称解析为一个 JSP 文件路径，并返回一个 InternalResourceView（内部资源视图）对象。通常用于将逻辑视图名称映射到 JSP 视图。
  
   ```xml
  <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
      <property name="prefix" value="/WEB-INF/views/"/>
      <property name="suffix" value=".jsp"/>
  </bean>
   ```
  
- **XmlViewResolver**：

  这个视图解析器从 XML 文件中读取视图配置，将逻辑视图名称解析为一个 AbstractUrlBasedView（基于 URL 的抽象视图）对象。

   ```xml
  <bean class="org.springframework.web.servlet.view.XmlViewResolver">
      <property name="location" value="/WEB-INF/views/views.xml"/>
  </bean>
   ```

- **FreeMarkerViewResolver**、**VelocityViewResolver**

  这些视图解析器用于将逻辑视图名称解析为 FreeMarker、Velocity 等模板引擎的视图对象。
  通过合适配置 ViewResolver，开发者可以根据项目的需求将逻辑视图名称映射到各种类型的实际视图对象上，从而实现灵活、可定制的视图解析策略。

## springmvc 的工作流程

Spring MVC 是 Spring Framework 中用于构建 Web 应用程序的一个模块，其工作原理如下：

1. **请求的到达**

   当客户端发起 HTTP 请求时，请求首先到达 Servlet 容器（如 Tomcat、Jetty），Servlet 容器根据部署描述符（web.xml）中的配置，将请求交给 Spring 的 DispatcherServlet。

2. **DispatcherServlet 的拦截**

   DispatcherServlet 是 Spring MVC 的核心控制器，它拦截所有的请求，并将它们分发到不同的处理器（Handler）上进行处理。
   DispatcherServlet 根据配置文件中的配置和注解扫描，将请求分发给相应的控制器（Controller）。

3. **处理器映射器（Handler Mapping）的使用**

   DispatcherServlet 使用 Handler Mapping 将请求映射到对应的处理器（Controller）。
   Handler Mapping 根据请求的 URL 和 Handler Mapping 的配置，确定哪个控制器将处理该请求。

4. **控制器的调用**

   一旦确定了要处理请求的控制器，DispatcherServlet 就会调用相应的控制器方法来处理请求。
   控制器方法负责执行业务逻辑，处理请求，并返回相应的数据模型或视图名称。

5. **处理器适配器（Handler Adapter）的使用**

   在调用控制器方法之前，DispatcherServlet 使用 Handler Adapter 将请求和控制器方法进行适配。
   Handler Adapter 将控制器方法的参数绑定到请求的数据，并执行控制器方法。

6. **处理器执行**

   控制器方法执行业务逻辑，处理请求。它可以调用业务逻辑层（Service）进行处理，并将结果存储在模型对象中。

7. **视图解析**

   控制器方法执行完毕后，DispatcherServlet 将模型对象传递给视图解析器（View Resolver）。
   视图解析器根据控制器返回的视图名称解析出实际的视图对象。

8. **视图渲染**

   视图解析器解析出实际的视图对象后，DispatcherServlet 将模型对象传递给视图对象，并请求视图对象渲染响应。
   视图对象将模型数据填充到视图中，生成最终的 HTML、JSON 或其他响应内容。

9. **响应的返回**

   渲染完成后，DispatcherServlet 将响应返回给客户端，完成请求-响应周期。

通过使用 DispatcherServlet、Handler Mapping、Handler Adapter、控制器和视图解析器等组件 SpringMVC 提供了一个灵活而强大的框架，用于构建各种类型的 Web 应用程序。

# Spring Boot

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

# Spring Boot 配置文件加载顺序

Spring Boot 读取配置文件的顺序如下：

1. 命令行参数：可以通过命令行参数的方式指定配置文件的路径。例如，使用 `java -jar myproject.jar --spring.config.location=file:/etc/myproject/application.yml` 指定配置文件路径。
2. `SPRING_CONFIG_LOCATION` 环境变量：Spring Boot 会优先读取该环境变量所指定的配置文件路径。
3. 项目根目录下的 `config/` 目录：Spring Boot 会读取项目根目录下的 `config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
4. 项目根目录：Spring Boot 会读取项目根目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。
5. `classpath:/config/` 目录：Spring Boot 会读取 `classpath:/config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
6. `classpath:/` 目录：Spring Boot 会读取 `classpath:/` 目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。

注：优先级从高到低排列。如果同一配置文件存在于多个位置，则优先级高的位置会覆盖优先级低的位置的配置。同时，高优先级的位置中的配置文件也可以包含低优先级位置中的配置文件。例如，项目根目录下的 `application.properties` 可以使用 `@PropertySource("classpath:custom.properties")` 引入 `custom.properties` 文件中的配置。



`bootstrap.yml` 是 Spring Boot 应用程序的一个特殊的配置文件，用于在应用程序启动之前加载。它的作用是在 Spring 应用程序的上下文创建之前加载外部配置，通常用于一些框架的初始化和配置，例如连接配置中心、加密/解密属性等。
与 `application.yml` 或 `application.properties` 不同，`bootstrap.yml` 的加载顺序更早，因此可以用来配置一些在应用程序启动之前就需要用到的参数。
它的配置格式与 `application.yml` 或 `application.properties` 类似，但主要用于一些与应用程序启动相关的配置。
一般情况下，`bootstrap.yml` 用于配置一些基础设施相关的配置，例如配置中心、分布式追踪、加密/解密等。

当你使用配置中心时，配置中心的配置会在应用程序启动时被加载，并且它的加载优先级高于本地的配置文件。加载顺序如下：

1. **配置中心的配置被加载**：
   - 如果你启用了配置中心（例如使用 Consul、Spring Cloud Config 等），应用程序会首先尝试从配置中心获取配置。这些配置会在应用程序启动时被加载，并覆盖本地配置文件中相同键的配置。

2. **本地配置文件的配置被加载**：
   - 如果配置中心中不存在某些配置项，或者配置中心不可用，应用程序会回退到本地的配置文件中查找配置。本地配置文件中的配置会在配置中心加载之后被加载，但在没有配置中心的情况下，本地配置文件中的配置会在应用程序启动时被最先加载。



# Spring Boot MVC 静态资源结构

## resources

`src/main/resources/` 是 Spring Boot（也是 Maven/Gradle Java 项目）的**资源目录**，它在编译后会被复制到最终的 `classpath`（通常是打包到 `classes` 里）。

在这里放的文件主要有：

- 配置文件（`application.properties`、`application.yml`）
- 消息文件（如国际化 `messages.properties`）
- 模板文件（如果用的是非嵌入式模板引擎，也可以放这里）
- 静态文件（某些情况）
- 证书、密钥、映射表、字典文件等需要打包到 JAR 的其他资源



Spring Boot 推荐把不同用途的文件放在不同的**子目录**下，而不是都堆在 `resources` 根目录里：

| 路径                                          | 用途                                                         |
| --------------------------------------------- | ------------------------------------------------------------ |
| `static/`                                     | 用于存放静态资源（HTML、CSS、JS、图片），对外直接可访问。    |
| `public/`                                     | 和 `static/` 一样，静态资源根目录。                          |
| `templates/`                                  | 模板引擎文件（如 Thymeleaf、Freemarker），需要后端渲染的页面。 |
| `META-INF/`                                   | 比如 SpringFactories，或者自定义注解的处理器等元信息。       |
| `application.properties` 或 `application.yml` | 配置文件，一般直接放在 `resources` 根目录。                  |



## Spring Thymeleaf 项目

模板框架静态文件:

- classpath:/templates

### thymeleaf 目录

ThymeleafProperties配置类可以看到 thymeleaf 的默认目录:

```java
public static final String DEFAULT_PREFIX = "classpath:/templates/";
public static final String DEFAULT_SUFFIX = ".html";
```

只要引入了thymeleaf之后, view 的转发最后都会转到 thymeleaf   

所以尽管在 static 和 templates 下都有 home.html, 但是 static 下面的永远不会被使用  

如果更改 templates 为 templates2, 而 thymeleaf 的默认目录不变, 那么尽管在 static 下面有 home.html, 依旧会报错

```
Error resolving template [home], template might not exist or might not be accessible by any of the configured Template Resolvers
```

## Spring JS 项目

静态文件处理方式

Spring Boot中的默认配置规定了静态资源的处理方式。

1. 默认的静态资源访问目录

   没有使用Thymeleaf或其他模板引擎时，Spring Boot会默认将静态资源放在**src/main/resources/static**目录下，并且会优先查找这些静态资源。
   因此，当访问根路径时（例如http://localhost:8080/），SpringBoot会自动查找 **index.html** 文件并返回给客户端，这是因为index.html通常被用作Web应用的默认首页。
   这种默认行为是出于方便考虑的，因为在许多Web应用中，index.html是默认的起始页面。
   如果需要自定义这个行为，可以修改Spring Boot的配置以更改默认的静态资源目录或修改默认的首页文件。

2. **WebMvcAutoConfiguration** 配置类
   org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.addResourceHandlers 这个方法会配置默认的静态资源处理器，指定了静态资源的查找位置，其中包括了 classpath:/static/

# Spring Boot 自动配置

## spring.factories

文件被放在 **src/main/resources** 目录下的 **META-INF** 子目录中。

spring.factories 是仿照 Java 的 **SPI（Service Provider Interface）** 机制而出现的技术。
用于声明Spring自动配置类，便于Spring启动时扫描这个文件，根据声明自动装配响应的组件。

## additional-spring-configuration-metadata.json

用于 Spring Boot 应用程序的自动配置元数据文件。
这个元数据文件包含了应用程序中使用的配置属性的描述信息，例如属性名称、类型、默认值、描述等。
IDE 可以根据这些元数据来提供智能代码补全、属性验证和提示等功能。

## org.springframework.boot.autoconfigure.AutoConfiguration.imports

`org.springframework.boot.autoconfigure.AutoConfiguration.imports` 是 Spring Boot 2.7 及更高版本中引入的新机制，用于替代传统的 `spring.factories` 文件中自动配置的注册方式。这个文件是 Spring Boot 自动配置机制的核心部分。

### 主要作用

1. **定义自动配置类**：
   - 该文件列出了所有应该被 Spring Boot 自动加载的自动配置类
   - 取代了之前版本中在 `META-INF/spring.factories` 中定义 `org.springframework.boot.autoconfigure.EnableAutoConfiguration` 的方式

2. **模块化自动配置**：
   - 允许更细粒度地控制自动配置的加载
   - 支持条件化加载配置类

3. **提高启动性能**：
   - 新的导入机制比 `spring.factories` 更高效
   - 减少了启动时的类加载和反射操作

### 文件位置和格式

该文件位于：
```
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

文件内容是简单的文本格式，每行一个全限定类名：
```
com.example.MyAutoConfiguration
com.example.AnotherAutoConfiguration
```

### 与传统方式的对比

| 特性     | AutoConfiguration.imports                                    | spring.factories   |
| -------- | ------------------------------------------------------------ | ------------------ |
| 引入版本 | Spring Boot 2.7+                                             | 旧版本             |
| 文件位置 | META-INF/spring/ 目录下                                      | META-INF/ 目录下   |
| 文件名称 | org.springframework.boot.autoconfigure.AutoConfiguration.imports | spring.factories   |
| 格式     | 每行一个类名                                                 | Properties文件格式 |
| 性能     | 更高效                                                       | 相对较低效         |

### 向后兼容

Spring Boot 2.7+ 仍然支持 `spring.factories` 方式，但推荐使用新的 `AutoConfiguration.imports` 方式。如果两者同时存在，`AutoConfiguration.imports` 会优先被使用。

### 实际应用示例

在开发自定义 Starter 时，你可以创建这个文件来注册你的自动配置类：

1. 创建文件：
   ```
   src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
   ```

2. 添加你的自动配置类：
   ```
   com.yourcompany.yourstarter.YourAutoConfiguration
   ```

这种新机制使得自动配置的管理更加简洁和高效，是 Spring Boot 现代化自动配置系统的重要组成部分。



# Spring Data

## Spring DATA Common

Spring Data Commons 是 Spring Data 项目中的一个核心模块，它为 Spring Data 生态系统中的各个子模块提供了一致的基础设施和支持。无论是操作关系型数据库（如 Spring Data JPA），还是非关系型数据库（如 Spring Data MongoDB），Spring Data Commons 都为它们提供了一套统一的编程模型和公共功能。

### Spring Data Commons 提供的功能

1. **Repository 抽象**：
   - 提供 `CrudRepository`、`PagingAndSortingRepository` 等基础接口，统一了数据访问层的设计模式，简化了 CRUD 操作。
   - 支持方法名解析查询，通过接口方法定义，自动生成相应的查询。

2. **流式 API**：
   - 自 Spring Data 2.x 版本起，引入流式操作，支持对查询结果的处理采用 Java 8 的 `Stream` API。

3. **分页和排序**：
   - 定义了分页和排序相关的通用接口和实现，如 `Pageable` 和 `Sort` 等，能轻松实现对数据库查询的分页和排序功能。

4. **Auditing**：
   - 提供审计功能，用于追踪实体的创建和修改信息，例如创建时间、创建者、修改时间、修改者等。

5. **Specification 和 QueryDSL 支持**：
   - 提供了基于 JPA 的 `Specification` 接口，支持通过组合不同条件构建动态查询。
   - 为子项目支持 QueryDSL，以便用类型安全且强大的 DSL 构建查询。

6. **Converter & Domain Events**：
   - 提供类型转换框架和事件机制，允许在实体状态变化时进行事件的发布和处理。

7. **基于元数据的配置**：
   - 允许通过注解和配置元数据来简化数据库操作的实现，尤其是在需要跨越多个数据源时。

### 使用场景

Spring Data Commons 并不直接用于数据库操作，而是为具体的数据访问模块提供基础支持。因此，它是一个后端基础模块，不同的数据访问模块（如 Spring Data JPA、Spring Data MongoDB 等）都会依赖于它来实现各自的功能。

### 示例应用

使用 Spring Data JPA 时，以下内容直接得益于 Spring Data Commons 的功能：

```java
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
}
```



## 四种事务的隔离级别

1. Read uncommitted(读未提交) -- 可能出现脏读,不可重复读
2. Read committed(读提交) -- 避免了脏读,出现了不可重复读
3. Repeatable read(可重复读取) -- 避免了不可重复读,可能出现幻读
4. Serializable(可序化,串行化) -- 避免了脏读,不可重复读和幻读



## 事务问题

- 丢失更新: 两个事务同时针对同一数据进行修改，可能导致数据丢失。
- 脏读: 一个事务读取到了另一个未提交的事务的数据，后者回滚导致前者读取的数据无效。
- 不可重复读: 一个事务多次读取同一数据，但在读取过程中另一个事务对该数据进行了修改，导致多次读取结果不一致。
- 幻读: 一个事务通过某些条件读取到一组数据，之后另一个事务插入了符合该条件的新数据，导致第一个事务再次读取时发现数据量增加。



## Java 事务管理

1. **Java 平台的局部事务支持**
   - JDBC：使用 `connection.commit()` 和 `connection.rollback()` 进行事务管理。
   - Hibernate：通过 `Session` 进行数据库访问期间的事务管理。
2. **Java 平台的分布式事务支持**



## Spring 事务管理

1. **Spring 事务的顶层接口**

   - `PlatformTransactionManager`：Spring 事务的核心接口，管理事务的定义和状态。
   - `TransactionDefinition`：定义事务的属性，如隔离级别、传播行为、超时时间、是否只读等。
   - `TransactionStatus`：表示整个事务处理过程中的状态，可用于查询状态、标记回滚、创建嵌套事务等。
     1. 使用提供的相应方法查询事务的状态
     2. 通过 setRollbackOnly() 方法标记房前事务以使其回滚
     3. 通过相应的 PlatformTransactionManager 支持 savePoint, 可以用过 TransactionStatus 在当前事务中创建嵌套事务

2. **DataSourceTransactionManager** PlatformTransactionManager的实现类

   - Transaction object
     承载当前事务的必要信息, 差不多就是 @Transacitonal 注解那个意思
   - TransactionSynchronization
     可以注册到事务处理过程中的回调接口, 相当于事务处理的逻辑, 比如报错了调用这个回调接口的内的回滚逻辑或者是提交逻辑
   - TransactionSynchronizationManager
     通过它来管理 TransactionSynchronization, 当前事务状态以及具体的事务资源
   - TransactionAttributeSource
     事务拦截器用于元数据检索的策略接口。实现知道如何获取事务属性, 无论是从配置, 源代码级别的元数据属性（如注释）还是其他任何位置
     类似与在 TransactionInterceptor 的配置中, 我们添加方法名作为 TransactionAttributeSource

3. **AbstractPlatformTransactionManager** 以模板方法的形式封装了固定的事务处理逻辑。

   以下为其中三个 AbstractPlatformTransactionManager 中的方法

    - getTransaction(@Nullable TransactionDefinition definition)
      主要目的是为了开启一个事务, 会在这个过程中判断是否已经有事务了, 然后根据 TransactionDefinition 中定义的信息,
      来决定传播行为
          1. 获取 transaction object 来判断是否存在当前事务
          2. 获取 debug 信息, 检查 TransactionDefinition 合法性
          3. 如果当前存在事务  
             PROPAGATION_NEVER - 直接抛出异常  
             PROPAGATION_NOT_SUPPORTED - 挂起当前事务  
             PROPAGATION_REQUIRES_NEW - 挂起当前事务, 并开启一个新的事务  
             PROPAGATION_NESTED - 则根据情况创建嵌套事务  
             PROPAGATION_REQUIRED/PROPAGATION_SUPPORTS - 直接构建 TransactionStatus 就返回了
          4. 如果不存在事务  
             PROPAGATION_MANDATORY - 直接抛出异常  
             PROPAGATION_REQUIRED/PROPAGATION_REQUIRES_NEW/PROPAGATION_NESTED - 都会开启新的事务  
             其他传播级别 - 返回不包含任何 transaction object 的 TransactionStatus
    - rollback(TransactionStatus status)  
      回滚事务
          1. 如果是嵌套事务, 则通过 TransactionStatus 释放 savepoint
          2. 如果 TransactionStatus 表示当前事务是一个新的事务, 则调用子类的 doRollback 方法回滚事务
          3. 如果当前事务存在, 并且要 rollbackOnly 状态被设置, 则调用子类的 doSetRollbackOnly 方法  
             触发 Synchronization 事件  
             清理事务资源
    - commit(TransactionStatus status)  
      提交事务  
      触发 Synchronization 事件  
      清理事务资源

   不过 spring 5 已经更新大量的其他方法

4. **Spring 提供编程式事务管理和声明式事务管理**

   - 编程式事务管理
     1. 直接使用 PlatformTransactionManager, 过于底层, 如果每个需要的地方都开发一套不符合设计模式的思想
     2. 使用 TransactionTemplate, 把 PlatformTransactionManager 相关的事务界定操作以及相关的异常处理进行了模板化封装,
        结合 Callback 接口
        使用这种方法想让事务回滚, 我们要么抛出免检异常, 要么设置 rollBackOnly 标志
   - 声明式事务管理
     结合 Spring Aop 来实现, 拦截器 + 元数据 + 代理对象
         1. ProxyFactory + TransactionInterceptor - 配置量太多, 可以从最底层上理解 Spring 声明式事务
         2. TransactionProxyFactoryBean - 把 TransactionInterceptor 纳入自身, 同样需要在每个需要事务的地方都进行配置
         3. BeanNameAutoProxyCreator - 进一步简化配置工作 todo
         4. Spring 2.x 的声明式事务配置
         5. 注解元数据驱动的声明式事务 - @Transactional 注解

5. **Spring的多数据源**

   1. 硬编码的式的实现方式, 直接在配置文件或者是代码中声明多个数据源, 然后多个数据源以不同的 BeanName 注册到容器中以实现多数据源
   2. 通过 Spring 提供的 AbstractRoutingDataSource 来实现多数据源(更适用于纯多数据源)
      如果接入了 Mybatis, 同样的, 对于 Mybatis 来说, 我们需要配置多个 Mybatis 的 SqlSessionFactoryBean
   3. 通过 Mybatis 插件的方式来实现多数据源(更适用于读写分离, Mybatis 可以识别 sql 的类型, 进而在不同的数据库执行)

6. **Spring的多数据源事务**

   1. 如果多数据源的方式是通过声明多个数据源来实现的, 那么多数据源的事务也按照声明多个 DataSourceTransactionManager
      来解决就行了
   2. 如果通过 AbstractRoutingDataSource 来实现多数据源的事务
      AbstractRoutingDataSource 说白了就是对第一点进行了一些巧妙的封装, 我们可以通过特定的类型来获得特定的数据库访问资源
      使用这种方式的下的事务完全由 spring 来掌控, 不管是什么框架, 比如 spring 的 JdbcTemplate, 或者是 Mybatis 的方式来访问数据库
      最终访问数据库的方式都会落到具体数据库给的 jdbc 驱动上, 都会通过 javax.sql.DataSource$getConnection() 方法来获取数据库连接,
      而事务的提交也最终都会在 connect 的基础上进行
      所以在 AbstractRoutingDataSource 的源码中我们可以看到, AbstractRoutingDataSource 重写的 getConnection
      方法会根据我们给的数据库类型, 帮我们找到对应的数据源然后返回
      最终事务会基于这个 connect 进行操作
      // todo 2023/02/25 需要看源码
   3. 使用 AbstractRoutingDataSource + 自定义注解
      使用这种方式特别需要注意的一点是, 我们自己自定义的切换数据源的切面的执行顺序一定要在 Spring 的事务切面之前执行,
      不然会导致数据源的切换不成功
      如果我们在 Spring 的事务切面之后执行, 这个时候 Spring 的事务切面已经去获取了一个 connect, 可想而知, 这个 connect
      是从默认数据源那里获取的, 所以无论执行什么操作, 最终都是对默认数据源进行操作

   在
   TransactionAspectSupport$invokeWithinTransaction 方法中可以看到, 在事务开始前我们首先会去获取一个 connect, 然后把这个 connect 信息放入 TransactionSynchronizationManager
   对于 Mybatis 来说, 它会通过它的 BaseExecutor$getConnection 方法在 TransactionSynchronizationManager 中拿到我们放入的
   connect, 这样当 Mybatis 的语句执行完之后,
   Spring 会通过它自己生成的 TransactionInfo 里包含的信息来提交或者回滚事务

7. **Springboot的多数据源+事务**

   1. DataSourceAutoConfiguration - 这是 springboot 的数据源自动配置
   2. dynamic-datasource-spring-boot-starter框架

   spring的动态多数据源
   使用 AbstractRoutingDataSource 类来根据运行时的条件切换不同的数据源
   使用 ChainedTransactionManager 类来处理多个数据源的分布式事务
   使用 @Qualifier 注解和 @Transactional 注解来指定不同的事务管理器

## spring data jdbc

## spring JDBC

`spring-boot-starter-jdbc` 是 Spring Boot 提供的一个启动器模块，用于简化 JDBC （Java Database Connectivity）开发。
它包含了一些常用的依赖和自动配置，可以帮助开发者快速搭建基于 JDBC 的数据访问层。

### 主要特性

1. **自动配置**：
   - 自动配置 `DataSource`，如果项目中存在相应的数据库驱动。也正是因为这个原因,这个将作为spring访问数据库的最基本的依赖,mybatis也会引用这个依赖,但是springredis是不会引入这个依赖的
   - 自动配置 `JdbcTemplate`，用于简化 JDBC 操作。

2. **默认依赖**：
   - 包含 Spring JDBC 和其他必要的库，无需手动配置。
   - 自动包含连接池（例如 HikariCP），作为默认的数据源连接池实现。

3. **简化的 JDBC 访问**：
   - 提供 `JdbcTemplate` 和 `NamedParameterJdbcTemplate`，简化常见的数据库操作。
   - 支持声明式事务管理。

### 典型结构

在一个典型的 Spring Boot 项目中，使用 `spring-boot-starter-jdbc` 可以极大简化 JDBC 的使用。一般步骤包括：

1. **添加依赖**：
   在 `pom.xml` 中添加 `spring-boot-starter-jdbc` 依赖：

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-jdbc</artifactId>
   </dependency>
   ```

2. **配置数据源**：
   在 `application.properties` 或 `application.yml` 文件中配置数据源信息：

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/mydb
   spring.datasource.username=root
   spring.datasource.password=secret
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

   以上例子以 MySQL 为例，连接信息会根据使用的数据库类型和实际情况调整。

3. **使用 JdbcTemplate**：
   通过自动注入 `JdbcTemplate` 或 `NamedParameterJdbcTemplate`，进行数据库操作。

   ```java
   @Service
   public class UserService {
   
       private final JdbcTemplate jdbcTemplate;
   
       public UserService(JdbcTemplate jdbcTemplate) {
           this.jdbcTemplate = jdbcTemplate;
       }
   
       public List<User> findAll() {
           String sql = "SELECT * FROM users";
           return jdbcTemplate.query(sql, (rs, rowNum) ->
               new User(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
       }
   
       public void save(User user) {
           String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
           jdbcTemplate.update(sql, user.getName(), user.getEmail());
       }
   }
   ```

### 优势

- **减少样板代码**：不需要手动管理数据库连接和关闭，`JdbcTemplate` 简化了查询、更新等操作。
- **自动化配置**：简便的配置，Spring Boot 会自动配置合适的 `DataSource` 并管理数据库连接池。
- **集成性强**：易于与其他 Spring 组件和模块集成，如 Spring Boot Test。

### 注意事项

- 默认的连接池是 HikariCP，尽管可以通过配置更改为其他连接池。
- 在生产环境中，建议仔细调整连接池参数以适应应用程序的负载。
- 尽管 `spring-boot-starter-jdbc` 提供了 JDBC 操作的简化方案，但当涉及复杂查询和高级功能时，可能需要考虑使用 JPA 或其他
  ORM 框架。

通过 `spring-boot-starter-jdbc`，开发者能够快速搭建一个基于 JDBC 的数据访问层，从而更专注于业务逻辑的实现。

# Spring 多数据源

spring默认支持配置单个数据源和多个数据源

- 单数据源形式通过 spring.datasource.xxx 进行配置
- 多数据源需要手动实现，spring 多数据源的实现基于 ThreadLocal 来实现，spring 通过在线程内设置线程的数据源标记来确定当前线程在获取数据库连接的时候具体使用哪个数据源

要使用 spring 多数据源需要准备如下东西：

1. 多个数据源需要进行真实的配置
2. 多个数据源如何进行区分和切换

实现的方式也有两种

1. 数据库的切换基于代码内
2. 数据库的切换基于注解形式

主要的流程如下

1. 准备工作:
   - 一个枚举类，用于标识各个数据源，这个标记也就是 ThreadLocal 的类型参数
   - 一个用于切换线程标记的工具类，这个工具类的用途可以是在程序内部任何需要进行切换的地方使用，也可以是基于注解的形式时作切换使用
   - 一个实现了 spring 提供的实现多数据源的类 **AbstractRoutingDataSource** 类，这个类将作为 spring 名为 dataSource 的 bean 的替换。
     - 这个类的内部提供一个 targetDataSource 保存真实的多个数据源，targetDataSource 是一个Map，key为我们定义的枚举类，value 则为真实的数据源配置。
     - 这个类需要实现 determineCurrentLookupKey 这个方法，在 spring 执行数据库操作时，获取连接时会调用这个方法来获取当前线程的数据源标记
   
   如果使用注解形式来进行切换数据库的操作，那么还需要准备一个基于注解的切面，然后注解的属性设置为枚举类的值，切面的逻辑则为捕获相关需要进行切换的方法，然后执行切换线程标记的工具的方法。
   
   **注意：在 AbstractRoutingDataSource 中其实存在两个 Map ：targetDataSources 和 resolvedDataSources
   
   - `targetDataSource` 存储用户配置的原始数据源映射（逻辑名称 → 数据源对象/数据源引用）。
   
     数据源的查找键（如 JNDI 名称），需通过 `DataSourceLookup` 解析。 不过JNDI没用过，这里一般存的都是真实的数据库。
   
   - `resolvedDataSources` 存储最终解析后的数据源映射（逻辑名称 → 真实 `DataSource` 实例）。
   
2. 整体流程：
   1. 一个需要 db 的请求到达后端
   2. 后端接受请求后，在访问 db 前，首先进行数据库的线程标记切换
   3. spring 获取数据库连接依次会调用 getConnect() -> determineTargetDataSource() -> determineCurrentLookupKey()
      方法来获取当前线程的数据库标记。
   4. determineTargetDataSource 方法会根据 determineCurrentLookupKey 返回的 key 在 resolvedDataSources 这个 map 内获取真实的数据库连接，resolvedDataSources 这个 map 是由我们设置的 targetDataSource 得来的
   5. spring 通过这个线程标记获取真实的数据库连接，然后执行db请求，最终返回
