package com.hundsun.demo.spring.processor;

import com.hundsun.demo.spring.service.IgnoreAware;
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
 * @updateUser: h1123
 * @updateDate: 2022/11/17 21:26
 * @updateRemark:
 * @version: v1.0
 * @see :
 */

public class IgnoreBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // configurableListableBeanFactory.ignoreDependencyInterface(IgnoreAware.class);
    }
}
