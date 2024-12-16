package org.hulei.springdata.routingdatasource.config.parsing;

import org.hulei.springdata.routingdatasource.core.DataSourceToggleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hulei
 * @since 2024/8/27 17:28
 */

@Slf4j
public class RoutingParsingDataSource extends AbstractRoutingDataSource {

    /**
     * 默认的数据源
     */
    private DataSource defaultDataSource;

    /**
     * 所有的数据源
     */
    private Map<Object, Object> targetDataSource = new ConcurrentHashMap<>();

    /**
     * 设置默认数据源,这个方法在bean生成的时候才会调用,代码中没有显示调用的地方.
     * bean配置在 org.hulei.springdata.routingdatasource.config.parsing.RoutingDataSourceBeanRegister#registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry) 这个方法设置了
     *
     * @param defaultDataSource 默认数据源
     */
    public void setDefaultDataSource(DataSource defaultDataSource) {
        log.info("正在设置默认数据源, database: {}", defaultDataSource.toString());
        this.defaultDataSource = defaultDataSource;
    }

    /**
     * 方法同 setDefaultDataSource 一致
     *
     * @param targetDataSource 目标数据源
     */
    public void setTargetDataSource(Map<Object, Object> targetDataSource) {
        log.info("正在设置多数据源, databaseSet: {}", targetDataSource.keySet());
        this.targetDataSource = targetDataSource;
    }

    @Override
    public void afterPropertiesSet() {

        super.setTargetDataSources(targetDataSource);

        super.setDefaultTargetDataSource(defaultDataSource);

        // 执行 AbstractRoutingDataSource 的赋值操作
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceToggleUtil.get();
    }
}
