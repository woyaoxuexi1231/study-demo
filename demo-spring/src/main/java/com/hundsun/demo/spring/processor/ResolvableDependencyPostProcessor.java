package com.hundsun.demo.spring.processor;

import com.hundsun.demo.spring.service.ResolvableDependencyA;
import com.hundsun.demo.spring.service.ResolvableDependencyB;
import com.hundsun.demo.spring.service.impl.ResolvableDependencyImplA;
import com.hundsun.demo.spring.service.impl.ResolvableDependencyImplB;
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
 * @updateUser: h1123
 * @updateDate: 2022/11/17 22:07
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class ResolvableDependencyPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        configurableListableBeanFactory.registerResolvableDependency(ResolvableDependencyA.class,new ResolvableDependencyImplA());
        configurableListableBeanFactory.registerResolvableDependency(ResolvableDependencyB.class,new ResolvableDependencyImplB());

    }
}
