package com.hundsun.demo.springboot.spring;

import com.hundsun.demo.springboot.SpringbootApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author hulei42031
 * @since 2024-06-04 17:25
 */

@RequestMapping("/springBean")
@RestController
@Slf4j
@Component
public class SpringBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    public SpringBean() {
        log.info("1. 这是一个spring bean的生命周期的第一步: 实例化");
    }

    SimpleSpringBean simpleSpringBean;

    @Autowired
    public void setSimpleSpringBean(SimpleSpringBean simpleSpringBean) {
        log.info("2. 第二步: 属性填充");
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
        log.info("3. 在bean实例化完成之后, 依赖注入完成之后, 会调用 setBeanName {}, 这个方法的作用仅仅是可以知道一个bean在factory内他的真实名字", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("4. setBeanFactory 可以知道这个bean对应的bean工厂是什么, 可以在这个阶段获取相关的bean, 但是一般DI的形式通过其他方式进行");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("5. setApplicationContext, 一般通过这个方法获取上下文, 需要对应实现 ApplicationContextAware 这个接口");
    }

    @PostConstruct
    public void init() {
        log.info("6. @PostConstruct, 这个方法将在实例化和依赖注入完成之后调用, 和InitializingBean接口实现的功能类似,不过这个方法比InitializingBean接口更先调用");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Bean 实例化和依赖注入完成之后，初始化之前, 这个方法相当于实现了 javax.annotation.PostConstruct
        log.info("6. afterPropertiesSet, 这个方法将在实例化和依赖注入完成之后调用, 在 initializeBean(beanName, exposedObject, mbd) 这个方法内执行, 在这个方法内, 执行此方法前会执行 setBeanName 和 setBeanFactory 方法");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("7. @PreDestroy, 容器摧毁时调用的方法, 此方法先于 DisposableBean 接口");
    }

    @Override
    public void destroy() throws Exception {
        // 相当于 javax.annotation.PreDestroy
        log.info("7. destroy, 相当于 @PreDestroy 注解");
    }

    @GetMapping("/getProtoTypeBean")
    public void getProtoTypeBean() {
        for (int i = 0; i < 10; i++) {
            ProtoTypeBean bean = SpringbootApplication.applicationContext.getBean(ProtoTypeBean.class);
            log.info("获取的多例bean为: {}", bean);
        }
    }

}

@Scope(scopeName = "singleton")
@Component
class SimpleSpringBean {

}

@Scope(scopeName = "prototype")
@Component
class ProtoTypeBean {

}