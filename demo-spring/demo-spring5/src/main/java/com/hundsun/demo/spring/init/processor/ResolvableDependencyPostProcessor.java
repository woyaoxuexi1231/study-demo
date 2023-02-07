package com.hundsun.demo.spring.init.processor;

import com.hundsun.demo.java.pattern.structural.proxy.MySQLService;
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
         */
        configurableListableBeanFactory.registerResolvableDependency(MySQLService.class, configurableListableBeanFactory.getBean("mySQLServiceProxy"));
    }
}
