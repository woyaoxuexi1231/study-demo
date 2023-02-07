package com.hundsun.demo.spring.init.processor;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.processor
 * @className: BeanFactoryPostProcessorTest
 * @description:
 * @author: h1123
 * @createDate: 2023/2/6 21:20
 */

public class BeanFactoryPostProcessorTest {

    /*
    BeanFactoryPostProcessor - spring的后置处理器
    BeanFactoryPostProcessor 是 Spring 的一种扩容机制, 该机制允许我们在容器实例化对象之前, 对注册到容器的 BeanDefinition 所保存的信息做相应的修改

    BeanPostProcessor 会处理容器内所有符合条件的实例化后的实例对象, 通常的场景是处理标记接口实现类, 或者为当前对象提供 *代理对象
    ApplicationContext 对应的那些 Aware 接口实际上就是通过 BeanPostProcessor 的方式进行处理的

    当 ApplicationContext 中每个对象的实例化过程走到 BeanPostProcessor 前置处理这一步时,
    ApplicationContext 容器会检测到之前注册到容器的 ApplicationContextAwareProcessor 这个 BeanPostProcessor 的实现类,
    然后就会调用其 postProcessBeforeInitialization()方法, 检查并设置 Aware 相关依赖

    IgnoreBeanFactoryPostProcessor 和 ResolvableDependencyPostProcessor两个测试类
     */
}
