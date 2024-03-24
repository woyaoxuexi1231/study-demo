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
        // log.info("this is PrintSmartInstantiationAwareBeanPostProcessor");
        return SmartInstantiationAwareBeanPostProcessor.super.getEarlyBeanReference(bean, beanName);
    }
}
