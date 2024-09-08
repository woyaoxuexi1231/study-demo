package com.hundsun.demo.spring.init.processor;

import com.hundsun.demo.spring.MySQLService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.processor
 * @className: ResolvableDependencyPostProcessor
 * @description:
 * @author: h1123
 * @createDate: 2022/11/17 22:07
 */

public class ResolvableDependencyPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        /*
        该方法的主要作用就是指定该类型接口, 如果外部要注入该类型接口的对象, 则会注入我们指定的对象, 而不会去管其他接口实现类
        这里我们实现一个简单的代理模式

        `configurableListableBeanFactory.registerResolvableDependency` 方法是Spring框架中的一个功能，它允许开发者在Spring容器中注册自定义的依赖解析。这意味着可以动态地添加可以自动装配的依赖，而无需在容器的Bean定义中显式配置它们。
        具体来说，这个方法允许你为特定类型的依赖注入自定义的解析策略。这是通过注册一个与依赖类型相匹配的对象来完成的。当Spring容器需要自动装配这个类型的依赖时，它将使用你提供的对象作为依赖的值。

        这个功能特别有用在以下情况：

        - 当你想要为特定类型提供一个共享实例，而不是每次注入时都创建一个新实例。
        - 当需要注入的依赖并不是简单的Bean，可能是需要通过某种特定方式计算或获取的对象，例如，从一个外部服务获取的数据，或是需要根据环境不同而变化的配置值。
        - 当你希望Spring管理的Bean之外的对象能够被注入到Spring管理的Bean中，而这些对象的创建不受Spring控制时。

        使用 `registerResolvableDependency` 方法可以增加应用的灵活性和动态性，使得Spring容器更加强大和可配置。然而，这也要求开发者对Spring的工作原理有深入的理解，以确保正确使用这一高级功能，避免潜在的问题。
         */
        configurableListableBeanFactory.registerResolvableDependency(MySQLService.class, configurableListableBeanFactory.getBean("mySQLServiceProxy"));
    }
}
