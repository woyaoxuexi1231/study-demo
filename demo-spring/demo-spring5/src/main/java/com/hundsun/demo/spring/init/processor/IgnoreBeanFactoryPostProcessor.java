package com.hundsun.demo.spring.init.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.processer
 * @className: IgnorePostProcesser
 * @description:
 * @author: h1123
 * @createDate: 2022/11/17 21:26
 */

public class IgnoreBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        /*
        这个方法用于忽略指定接口类型的 bean 的自动注入(不会被自动注入到其他类中)

        这行代码是在使用Spring框架中的高级功能，具体来说，它是用于告诉Spring容器在自动装配（autowire）过程中忽略某些接口类型的依赖。
        在Spring框架中，`ConfigurableListableBeanFactory` 是一个强大的工厂类，用于管理Bean的定义、创建、装配、获取等。它提供了细粒度的控制，包括能够修改Bean定义以及初始化过程中忽略某些自动装配的依赖。
        当你调用 `ignoreDependencyInterface` 方法时，你可以指定一个接口（此处为 `IgnoreAware`），之后Spring容器在自动装配Bean时，如果遇到了实现了这个接口的类，它将不会考虑这个接口的自动装配依赖。这通常用于某些特殊场景，比如当你想要控制某些自动装配的行为，或者避免由于自动装配导致的循环依赖。
        简而言之，这段代码是Spring的高级应用，用于自定义Spring容器的自动装配行为，避免在某些情况下的依赖性注入问题。
        */
        configurableListableBeanFactory.ignoreDependencyInterface(IgnoreAware.class);
    }
}
