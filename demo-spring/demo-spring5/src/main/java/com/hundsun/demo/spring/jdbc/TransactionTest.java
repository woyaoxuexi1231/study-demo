package com.hundsun.demo.spring.jdbc;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: TransactionTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 21:50
 */

public class TransactionTest {

    /*
    Java 事务

    > 四种事务的隔离级别
    > 1. Read uncommitted(读未提交) -- 可能出现脏读,不可重复读
    > 2. Read committed(读提交) -- 避免了脏读,出现了不可重复读
    > 3. Repeatable read(可重复读取) -- 避免了不可重复读,可能出现幻读
    > 4. Serializable(可序化,串行化) -- 避免了脏读,不可重复读和幻读
    >
    > 丢失更新 : 两个事务同时针对统一数据进行修改, 会存在数据丢失的情况
    > 脏读 : 对于两个事务, t1 读取到了 t2 未提交的数据, 之后如果 t2 回滚了,导致 t1 读取的数据是无效的
    > 不可重复读 : 对于两个事务, t1 读取到了某一个数据, 但是之后 t2 进行了更新, 导致 t1 再次读取这个数据的时候, 这个数据与之前读取的不一致
    > 幻读 : 对于两个事务, t1 通过条件读取到了某些数据, 但是之后 t2 进行了插入操作, 导致 t1 再次通过同一个条件或者数据的时候, 发现获取到的数据比之前的数据多了

    Java 事务管理
    1. Java 平台的局部事务支持
        在 JDBC 中, 我们可以找到 Java 平台为我们提供的最基础的关系数据库的 API, connection.commit()/connection.rollback()
        在 Hibernate 中, 我们可以使用 Session 进行数据库访问期间的事务管理
    2. Java 平台的分布式事务支持
        todo


    Spring 的事务管理
    PlatformTransactionManager - Spring 事务抽象结构的核心接口, 内部维持对 TransactionDefinition 和 TransactionStatus 的使用
    TransactionDefinition - 主要定义有哪些事务属性可以指定
        1. 事务的隔离级别
        2. 事务的传播行为
        3. 事务的超时时间
        4. 是否为只读事务
    TransactionStatus - 定义表示整个事务处理过程中的事务状态
        1. 使用提供的相应方法查询事务的状态
        2. 通过 setRollbackOnly() 方法标记房前事务以使其回滚
        3. 通过相应的 PlatformTransactionManager 支持 savePoint, 可以用过 TransactionStatus 在当前事务中创建嵌套事务
    transaction object - 承载当前事务的必要信息
    TransactionSynchronization - 可以注册到事务处理过程中的回调接口
    TransactionSynchronizationManager - 通过它来管理 TransactionSynchronization, 当前事务状态以及具体的事务资源
    AbstractPlatformTransactionManager - 以模板方法的形式封装了固定的事务处理逻辑
        getTransaction(@Nullable TransactionDefinition definition)
            主要目的是为了开启一个事务, 会在这个过程中判断是否已经有事务了, 然后根据 TransactionDefinition 中定义的信息, 来决定传播行为
            1. 获取 transaction object 来判断是否存在当前事务
            2. 获取 debug 信息, 检查 TransactionDefinition 合法性
            3. 如果当前存在事务 -
                PROPAGATION_NEVER - 直接抛出异常
                PROPAGATION_NOT_SUPPORTED - 挂起当前事务
                PROPAGATION_REQUIRES_NEW - 挂起当前事务, 并开启一个新的事务
                PROPAGATION_NESTED - 则根据情况创建嵌套事务
                PROPAGATION_REQUIRED/PROPAGATION_SUPPORTS - 直接构建 TransactionStatus 就返回了
            4. 如果不存在事务 -
                PROPAGATION_MANDATORY - 直接抛出异常
                PROPAGATION_REQUIRED/PROPAGATION_REQUIRES_NEW/PROPAGATION_NESTED - 都会开启新的事务
                其他传播级别 - 返回不包含任何 transaction object 的 TransactionStatus
        rollback(TransactionStatus status)
            回滚事务
            1. 如果是嵌套事务, 则通过 TransactionStatus 释放 savepoint
            2. 如果 TransactionStatus 表示当前事务是一个新的事务, 则调用子类的 doRollback 方法回滚事务
            3. 如果当前事务存在, 并且要 rollbackOnly 状态被设置, 则调用子类的 doSetRollbackOnly 方法
            触发 Synchronization 事件
            清理事务资源
        commit(TransactionStatus status)
            提交事务
            触发 Synchronization 事件
            清理事务资源
        不过 spring 5 已经更新大量的其他方法
    Spring 提供编程式事务管理和声明式事务管理
        编程式事务管理
            1. 直接使用 PlatformTransactionManager, 过于底层, 如果每个需要的地方都开发一套不符合设计模式的思想
            2. 使用 TransactionTemplate, 把 PlatformTransactionManager 相关的事务界定操作以及相关的异常处理进行了模板化封装, 结合 Callback 接口
                使用这种方法想让事务回滚, 我们要么抛出免检异常, 要么设置 rollBackOnly 标志
        声明式事务管理
            结合 Spring Aop 来实现, 拦截器 + 元数据 + 代理对象
            1. ProxyFactory + TransactionInterceptor - 配置量太多, 可以从最底层上理解 Spring 声明式事务
            2. TransactionProxyFactoryBean - 把 TransactionInterceptor 纳入自身, 同样需要在每个需要事务的地方都进行配置
            3. BeanNameAutoProxyCreator - 进一步简化配置工作 todo
            4. Spring 2.x 的声明式事务配置
            5. 注解元数据驱动的声明式事务 - @Transactional 注解
     */



}
