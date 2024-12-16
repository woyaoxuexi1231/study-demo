## Spring DATA Common

Spring Data Commons 是 Spring Data 项目中的一个核心模块，它为 Spring Data 生态系统中的各个子模块提供了一致的基础设施和支持。无论是操作关系型数据库（如
Spring Data JPA），还是非关系型数据库（如 Spring Data MongoDB），Spring Data Commons 都为它们提供了一套统一的编程模型和公共功能。

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

Spring Data Commons 并不直接用于数据库操作，而是为具体的数据访问模块提供基础支持。因此，它是一个后端基础模块，不同的数据访问模块（如
Spring Data JPA、Spring Data MongoDB 等）都会依赖于它来实现各自的功能。

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

### 总结

Spring Data Commons 是 Spring
数据访问模块的基础，它提供了通用的数据访问和操作模板，极大地简化了由不同数据库特性带来的复杂性。通过统一的编程模型和多种灵活的实现机制，开发者可以更加专注于应用逻辑的实现。无论是传统的关系型数据库，还是新兴的
NoSQL 数据库，Spring Data Commons 都提供了良好的支持。

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