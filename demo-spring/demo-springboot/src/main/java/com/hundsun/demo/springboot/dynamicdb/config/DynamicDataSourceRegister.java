package com.hundsun.demo.springboot.dynamicdb.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: DynamicDataSourceRegister
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 21:35
 */

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {

    /*
    这是一种更通用化的动态数据源配置方式

    我们自己定义一个动态数据源的注册类, 通过自己管理所有的数据源来达到目的
     */

    /**
     * 所有的数据源
     */
    private Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    /**
     * 默认的数据源
     */
    private DataSource defaultDataSource;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        /*
        1. 这里我们可以不需要再强制要求数据源的名称是什么, 可以获取配置文件中的属性来决定数据源的名字
        2. 初始化需要用到的所有数据源, 并把名字和数据源绑定起来, 然后保存起来
         */
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 然后我们这里自己注册一个 DataSource 的 BeanDefinition
        GenericBeanDefinition definition = new GenericBeanDefinition();
        // 设置 Bean 的 class 类型
        definition.setBeanClass(DataSource.class);
        // 自定义合成类
        definition.setSynthetic(true);
        // 设置属性
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue("dataSourceMap", this.dataSourceMap);
        propertyValues.addPropertyValue("defaultDataSource", this.defaultDataSource);

        // 注册到容器里去
        registry.registerBeanDefinition("dataSource", definition);
    }
}
