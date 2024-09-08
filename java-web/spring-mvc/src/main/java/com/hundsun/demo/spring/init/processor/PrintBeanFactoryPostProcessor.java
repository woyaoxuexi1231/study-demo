package com.hundsun.demo.spring.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.init.processor
 * @className: PrintBeanFactroyPostProcessor
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/23 15:53
 */

@Slf4j
public class PrintBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("this is PrintBeanFactoryPostProcessor");
    }
}
