package org.hulei.spring.xml.init;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hulei.spring.xml.init.processor.IgnoreBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-22 16:21
 */

@Slf4j
@Data
public class SimpleServiceImpl implements SimpleService, ApplicationContextAware {

    /**
     * 这是一个不会被自动注入的属性
     */
    IgnoreBean ignoreBean;

    ExternalService externalService;

    /**
     * 这里通过 ApplicationContextAware 注入 spring的容器
     */
    private ApplicationContext applicationContext;

    /**
     * 一个在容器初始化阶段会被调用的方法
     */
    public void init() {
        log.info("The container is initializing...");
    }

    /**
     * 一个在容器销毁阶段会被调用的方法
     */
    public void destroy() {
        log.info("The container is being destroyed...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void printIgnoreBean() {
        log.info("注入了一个被忽略了自动注入的接口的实现类, {}", ignoreBean);
    }

    public void printExternalService() {
        log.info("注入了一个通过registerResolvableDependency注册的依赖, {}", externalService);
    }
}
