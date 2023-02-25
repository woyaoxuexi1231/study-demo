package com.hundsun.demo.spring.aop;

import org.springframework.context.annotation.Configuration;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.aop
 * @className: AopTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/11 13:21
 */
@Configuration
public class AopTest {

    /*
    AOP 的相关概念
        JoinPoint - 在确切的某个点进行织入操作的那个点就是一个 JoinPoint
        Pointcut - JoinPoint 是我们逻辑上的一个点, 而 Pointcut 在代码层面帮我们描述 JoinPoint 且 Pointcut可以描述由一堆 JoinPoint 的集合, SpringAop 中的 @PointCut 注解
        Advice - 表示在这个 JoinPoint 需要添加的横切逻辑, 对应我们的 @Before, @After, @Around
            Around Advice - 在 JoinPoint 前后都执行的横切逻辑
            Introduction - 与其他类型的 Advice 关注点不同的是, Introduction 根据它可以完成的功能而区别与其他类型的 Advice
        Aspect - 代表的是系统中由 Pointcut 和 Advice 组合而成的一个 AOP 概念实体, 一个完整的横切逻辑的描述, 也就是我们定义的一个 Aspect 类
        Weaver - 织入器, 织入器为我们完成织入操作, 写完 Aspect 如果不织入是完全没有意义的
        Target Object - 目标对象

        AOP 主要思想是代理模式

    Spring AOP
        PointCut -
            spring 为我们提供了一些默认的 PointCut 实现类, 比如 NameMatchMethodPointcut, AnnotationMatchingPointcut...
            当然也可以自定义 PointCut 实现类, 后来 Spring 又提供了 @PointCut 注解以实现自定义的切入点
        Advice -
            Spring 也为我们提供了一些标准的 Advice 接口, 如 AfterReturningAdvice, MethodBeforeAdvice...
            Advice 的标准把 Advice 大致划分为两类, per-instance 和 per-instance
                per-class 该类型的 Advice 可以在目标对象类的所有实例之间共享, 不会为目标对象类保存任何状态或者添加新的特性
                per-instance 该类型不会在目标对象之间共享, 而且会为不同的实例对象保存他们各自的状态以及相关逻辑
        Aspect - spring 中也有一些默认的 Advisor(Aspect), 更多时候, 我们是自定义 Aspect
        Ordered - 切面顺序, order值越低优先级越高, 即在最外圈, 如果一个切面没有配置 @order 注解或者配置了但是没有手动设置优先级, 那么切面将以最低优先级执行
        Weaving - 万事俱备, 只欠东风! 在 Spring 5 的源码中可以找到 ProxyFactory 和 AspectJProxyFactory 两个织入器
            AopProxy - 代理实现机制的顶层抽象接口, 生成代理类的方法由这个接口的子类实现, 主要有 CglibAopProxy 和 JdkDynamicAopProxy
            AopProxyFactory - 根据传入的 AdvisedSupport 实例提供的信息来决定生成什么类型的 AopProxy
            AdvisedSupport - 生成代理对象需要的信息载体, 该类继承于 ProxyConfig, 实现了 Advised 接口
                ProxyConfig - 记载生成代理对象的控制信息, 比如 proxyTargetClass, optimize 以控制生成代理对象的机制是 cglib 还是 jdk
                Advised - 承载生成代理对象所需要的必要信息, 比如相关目标类, Advice, Advisor等
            ProxyFactory - 集 AopProxy 和 AdvisedSupport 与一身, 可以通过该类设置生成代理对象所需要的相关信息, 也可以通过这个类获得最终生成的代理对象
            ProxyCreatorSupport - 抽象自 ProxyFactory

            接下来到重头戏了, 前面所有的东西我们都可以脱离容器进行使用, 但是在 Spring 的大家庭中, 将 Spring Aop 和 Spring IoC 容器支持相结合, 才能发挥 Spring AOP 的最大作用
            ProxyFactoryBean - 它为持有它引用的对象返回 getObject() 方法所返回的对象
            ProxyFactoryBean + BeanPostProcessor = 自动代理
     */

}
