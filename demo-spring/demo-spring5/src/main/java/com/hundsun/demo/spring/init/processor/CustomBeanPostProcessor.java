package com.hundsun.demo.spring.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class CustomBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 实例化对象之前会调用这个方法
        if (beanName.equals("circleBean")) {
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                System.out.println(stackTraceElement);
            }
        }
        return bean; // 返回原始的 bean
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 实例化对象之后会调用这个方法(初始化之前,在实例化之后紧接着就会调这个方法)
        if (beanName.equals("circleBean")) {
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                System.out.println(stackTraceElement);
            }
        }
        return bean; // 返回原始的 bean
    }
}
