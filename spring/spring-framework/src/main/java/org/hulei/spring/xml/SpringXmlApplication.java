package org.hulei.spring.xml;

import lombok.extern.slf4j.Slf4j;
import org.hulei.spring.xml.aop.xml.PrintService;
import org.hulei.spring.xml.init.ExternalService;
import org.hulei.spring.xml.init.SimpleService;
import org.hulei.spring.xml.init.SimpleServiceImpl;
import org.hulei.spring.xml.init.listener.SimpleEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author hulei
 * @since 2024/10/13 19:12
 */

@Slf4j
public class SpringXmlApplication {

    static ApplicationContext applicationContext;

    public static void main(String[] args) {

        // 创建容器,以读取配置文件的方式开启容器
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", "simple.xml");
        applicationContext.publishEvent(new SimpleEvent("容器启动完成! "));

        // BeanFactoryPostProcessor干预的两个bean, 一个忽略了自动注入, 一个由通过registerResolvableDependency注入
        applicationContext.getBean(SimpleServiceImpl.class).printIgnoreBean();
        applicationContext.getBean(SimpleServiceImpl.class).printExternalService();

        try {
            applicationContext.getBean(ExternalService.class).serve();
        } catch (BeansException e) {
            log.error(e.getMessage());
        }

        // 这是一个完全xml配置的spring aop
        applicationContext.getBean(PrintService.class).print();

    }
}
