package com.hundsun.demo.springboot.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author hulei42031
 * @since 2024-06-04 17:25
 */

@Slf4j
@Component
public class SpringBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    public SpringBean() {
        System.out.println("1. 实例化");
    }

    SimpleSpringBean simpleSpringBean;

    @Autowired
    public void setSimpleSpringBean(SimpleSpringBean simpleSpringBean) {
        System.out.println("2. 属性填充");
        this.simpleSpringBean = simpleSpringBean;
    }

    @Override
    public void setBeanName(String name) {
        /*
        Bean 实例化之后、依赖注入之后

		try {
			populateBean(beanName, mbd, instanceWrapper);
			exposedObject = initializeBean(beanName, exposedObject, mbd);
		}
         */
        System.out.printf("3. setBeanName %s%n", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4. setBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("5. setApplicationContext");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Bean 实例化和依赖注入完成之后，初始化之前, 这个方法相当于实现了 javax.annotation.PostConstruct
        System.out.println("6. afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception {
        // 相当于 javax.annotation.PreDestroy
        System.out.println("7. destroy");
    }

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy");
    }

}

@Component
class SimpleSpringBean {

}
