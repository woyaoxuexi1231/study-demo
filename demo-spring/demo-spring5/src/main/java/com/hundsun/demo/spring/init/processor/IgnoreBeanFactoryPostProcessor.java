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
        // 这个方法用于忽略指定接口类型的 bean 的自动注入(不会被自动注入到其他类中)
        configurableListableBeanFactory.ignoreDependencyInterface(IgnoreAware.class);
    }
}
