package org.hulei.spring.xml.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.hulei.spring.xml.init.circle.CircleBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * @author woaixuexi
 * @since 2024/3/23 16:04
 */

@Slf4j
public class PrintInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        // 在 Bean 实例化之前调用
        if ("circleBean".equals(beanName)) {
            System.out.println("Before Instantiation: " + beanName);
            // 可以返回一个自定义的实例，或者返回 null 继续使用默认实例化过程
            return new CircleBean();
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        // 在 Bean 实例化之后，但在任何初始化回调方法被调用之前调用(属性填充之前)
        if ("circleBean".equals(beanName)) {
            System.out.println("After Instantiation: " + beanName);
            // 返回 false 表示不需要对此 Bean 执行后续的初始化
            return false;
        }
        return true;
    }


    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 在 Bean 的属性被注入后调用 方法之前是 postProcessPropertyValues
        if ("exampleBean".equals(beanName)) {
            System.out.println("Post Process Property Values: " + beanName);
            // 可以检查和修改属性值
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

}
