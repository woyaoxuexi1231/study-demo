package org.hulei.spring.xml.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 与普通的 `BeanPostProcessor` 不同，`MergedBeanDefinitionPostProcessor` 主要用于处理合并后的 Bean 定义（MergedBeanDefinition）。
 * <p>
 * 在 Spring 中，Bean 定义（BeanDefinition）是描述一个 Bean 的配置信息的对象。而在实际的应用程序上下文中，这些 Bean 定义可能会被合并（Merged）成一个最终的形式，以便于容器使用。合并后的 Bean 定义包含了所有的配置信息，以及所有的父类和接口等相关信息。
 * `MergedBeanDefinitionPostProcessor` 的作用就是在合并后的 Bean 定义被应用之前，对其进行进一步的处理。它可以对合并后的 Bean 定义进行检查、修改或者添加一些额外的信息。这使得开发人员可以在 Bean 被实例化之前，对其进行更加细致和灵活的处理。
 * 这个接口通常用于一些高级的、需要对 Bean 定义进行深入处理的场景，比如 Spring Boot 中的自动配置（auto-configuration）机制。开发人员可以实现这个接口，并注册为 Bean 后置处理器，以便在 Spring 容器初始化阶段对 Bean 定义进行额外的处理。
 *
 * @author woaixuexi
 * @since 2024/3/23 15:58
 */

@Slf4j
public class PrintMergedBeanDefinitionPostProcessor implements MergedBeanDefinitionPostProcessor {

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        // 在这里可以对 BeanDefinition 进行修改，比如添加额外的属性或者修改原有的属性
        // 方法执行的时间点在对象实例化完成之后暴露之前,对所有的bean都会进行一个增强
        if (beanName.equals("circleBean")) {
            beanDefinition.getPropertyValues().add("name", "MergedBeanDefinitionPostProcessor");
        }
    }

    @Override
    public void resetBeanDefinition(String beanName) {
        // 在这里可以清除与 BeanDefinition 相关的状态或缓存
        System.out.println("Resetting BeanDefinition for bean: " + beanName);
    }
}
