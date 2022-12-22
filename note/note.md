###### 2022-09-20

> Spring事务的传递

###### 2022-09-26

> 四种事务的隔离级别
> 1. Read uncommitted(读未提交) -- 可能出现脏读,不可重复读
> 2. Read committed(读提交) -- 避免了脏读,出现了不可重复读
> 3. Repeatable read(可重复读取) -- 避免了不可重复读,可能出现幻读
> 4. Serializable(可序化,串行化) -- 避免了脏读,不可重复读和幻读
>
> 丢失更新 : 两个事务同时针对统一数据进行修改,会存在数据丢失的情况  
> 脏读 : 对于两个事务,t1读取到了t2未提交的数据,之后如果t2回滚了,导致t1读取的数据是无效的  
> 不可重复读 : 对于两个事务,t1读取到了某一个数据,但是之后t2进行了更新,导致t1再次读取这个数据的时候,这个数据与之前读取的不一致
> 幻读 : 对于两个事务,t1通过条件读取到了某些数据,但是之后t2进行了插入操作,导致t1再次通过同一个条件或者数据的时候,发现获取到的数据比之前的数据多了

###### 2022-10-08

> JDK的动态代理  
> JDK动态代理的代理对象是在运行时生成的字节码对象,并非自己写的对象
> JDK动态代理需要我们自己写一个调用处理类(实现InvocationHandler接口的具体类)
> 1. 为什么JDK动态代理必须要实现接口
> 2. 为什么要继承proxy类
> ---------
> 通过Proxy源码可以发现,sun.misc.ProxyGenerator#generateClassFile这个方法在生成代理对象的时候会在我们传入的接口类中获取方法
> 生成的每个代理对象都将继承proxy,并且传给proxy一个指定的invokeHandler,proxy把代理对象的共有属性抽象出来.
> 由此,Java不支持多继承以及满足我们的代理对象需要与被代理的对象之间建立联系,我们只能去实现一个接口

###### 2022-10-09 - 2022-10-10

> CGLIB  
> 拦截器 - 需要我们自己写一个拦截器(实现MethodInterceptor接口)
> ,这个拦截器定义我们在调用目标方法前后进行的增强逻辑,以及创建代理对象的逻辑  
> 回调过滤器 - 实现CallbackFilter接口,可以自定义回调函数的过滤器,配合Callback[]可以实现不同的方法调用不同的回调函数  
> 代理对象 - 生成的代理对象会继承我们的目标对象,然后实现Factory接口

###### 2022-10-31

> Spring AOP  
> Joinpoint -- 在确切的某个点进行织入操作的那个点就是一个Joinpoint  
> Pointcut -- Joinpoint是我们逻辑上的一个点, 而Pointcut在代码层面帮我们描述Joinpoint,
> 且Pointcut可以描述由一堆Joinpoint的集合  
> Advice -- 表示在这个Jointpoint需要添加的横切逻辑  
> Around Advice -- 在Joinpoint前后都执行的横切逻辑  
> Introduction -- 与其他类型的Advice关注点不同的是, Introduction根据它可以完成的功能而区别与其他类型的Advice  
> Aspect -- 代表的是系统中由Pointcut和Advice组合而成的一个AOP概念实体,一个完整的横切逻辑的描述
> -------------------------
> per-class类型的Aspect 包括 Advice 和 Around Advice  
> per-instance类型的Aspect 包括 Introduction
> 
> 
###### 2022-11-24
spring 读取配置文件的顺序如下:   
–file:./config/  
–file:./  
–classpath:/config/  
–classpath:/  