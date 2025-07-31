package org.hulei.spring.xml.init.processor;

import lombok.extern.slf4j.Slf4j;
import org.hulei.spring.xml.init.ExternalService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author hulei
 * @since 2024/10/13 19:35
 */

@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        /*
        这个方法用于忽略指定接口类型的 bean 的自动注入(不会被自动注入到其他类中)
        */
        log.info("忽略了 IgnoreAware 接口实现类的自动注入功能");
        beanFactory.ignoreDependencyInterface(IgnoreAware.class);
        /*
        1. 第三方库的对象注入
        2. 共享对象实例注入
        3. 环境对象注入
        总结来说常见的使用场景包括外部服务、系统资源、动态生成的对象或框架级别的依赖注入。
        所以这个方法一般用于容器创建早期,spring注册容器,资源加载器,时间发布器都使用了这个方法
         */
        log.info("注入一个自定义的外部类 ExternalService ");
        beanFactory.registerResolvableDependency(ExternalService.class, new ExternalService());
    }
}
