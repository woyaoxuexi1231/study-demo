package com.hundsun.demo.springboot.db.dynamicdb.config.parsing;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean定义的注册器,这种方式更加灵活和实用,我们不一定要硬性要求我们的数据库一定要是什么名字,名字是在配置文件中灵活确定的.
 * 同样的,高度灵活的配置也会付出代价,我们怎么事先在代码里知道我们需要切换到什么数据库呢?如果要进行数据库的切换就需要用其他手段了,我们需要扫描配置的数据源有哪些,然后需要有额外的地方存储数据源的标记信息
 *
 * @author h1123
 * @since 2023/2/25 21:35
 */

@Slf4j
@Conditional(EnableParsingRoutingDBConfig.class)
public class RoutingDataSourceBeanRegister implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {

    /*
    这是一种更通用化的动态数据源配置方式

    我们自己定义一个动态数据源的注册类, 通过自己管理所有的数据源来达到目的
     */

    /**
     * 所有配置的数据源
     */
    private Map<String, Map<String, String>> dataSourceProperties = new ConcurrentHashMap<>();

    /**
     * 所有的数据源
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

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

        for (PropertySource<?> propertySource : ((StandardEnvironment) environment).getPropertySources()) {
            if (propertySource instanceof org.springframework.core.env.MapPropertySource) {
                Map<String, Object> source = ((org.springframework.core.env.MapPropertySource) propertySource).getSource();

                // 遍历每个属性，检查是否以指定前缀开头
                for (String key : source.keySet()) {
                    if (key.startsWith("spring.datasource.parsing")) {
                        String[] split = key.split("\\.");
                        if (dataSourceProperties.containsKey(split[3])) {
                            dataSourceProperties.get(split[3]).put(split[4], environment.getProperty(key));
                        } else {
                            Map<String, String> map = new ConcurrentHashMap<>();
                            map.put(split[4], environment.getProperty(key));
                            dataSourceProperties.put(split[3], map);
                        }
                    }
                }
            }
        }

        buildDataSource(dataSourceProperties);

        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            defaultDataSource = entry.getValue();
            break;
        }
    }

    private void buildDataSource(Map<String, Map<String, String>> dsMap) {

        for (Map.Entry<String, Map<String, String>> entry : dsMap.entrySet()) {
            String driverClassName = entry.getValue().get("driverClassName");
            if (com.mysql.cj.jdbc.Driver.class.getName().equals(driverClassName)) {
                // 我这里使用 HikariDataSource
                HikariConfig config = new HikariDataSource();
                config.setJdbcUrl(entry.getValue().get("url"));
                config.setUsername(entry.getValue().get("username"));
                config.setPassword(entry.getValue().get("password"));
                config.setPoolName(String.format("parsing-%s", entry.getValue().get("url").substring("jdbc:mysql://".length(), entry.getValue().get("url").lastIndexOf("?"))));
                dataSourceMap.put(entry.getKey(), new HikariDataSource(config));
            }
        }
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 然后我们这里自己注册一个 DataSource 的 BeanDefinition
        GenericBeanDefinition definition = new GenericBeanDefinition();
        // 设置 Bean 的 class 类型
        definition.setBeanClass(RoutingParsingDataSource.class);
        // 自定义合成类
        definition.setSynthetic(true);
        // 设置属性
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue("targetDataSource", this.dataSourceMap);
        propertyValues.addPropertyValue("defaultDataSource", this.defaultDataSource);

        // 注册到容器里去
        registry.registerBeanDefinition("dataSource", definition);
    }
}
