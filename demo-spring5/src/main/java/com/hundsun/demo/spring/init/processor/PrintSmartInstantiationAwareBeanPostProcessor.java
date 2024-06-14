package com.hundsun.demo.spring.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.lang.reflect.Constructor;

/**
 * @author woaixuexi
 * @since 2024/3/23 16:03
 */

@Slf4j
public class PrintSmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {


    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        log.info("this is PrintSmartInstantiationAwareBeanPostProcessor. beanName: {}", beanName);
        return SmartInstantiationAwareBeanPostProcessor.super.predictBeanType(beanClass, beanName);
    }

    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        // log.info("this is PrintSmartInstantiationAwareBeanPostProcessor");
        return SmartInstantiationAwareBeanPostProcessor.super.determineCandidateConstructors(beanClass, beanName);
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        // 在 AOP 功能中，可以使用 getEarlyBeanReference 方法提前创建代理对象，以便在 Bean 实例化完成后立即应用切面逻辑。
        // 在需要提前处理 Bean 的某些属性或状态的情况下，可以在此方法中进行相关操作。
        // 在org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator.getEarlyBeanReference就实现了代理对象的生成
        // log.info("this is PrintSmartInstantiationAwareBeanPostProcessor");
        return SmartInstantiationAwareBeanPostProcessor.super.getEarlyBeanReference(bean, beanName);
    }
}
