package org.hulei.keeping.server.db.dynamicdb.config.parsing;

import org.hulei.keeping.server.db.dynamicdb.core.DataSourceToggleUtil;
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

    public void setDefaultDataSource(DataSource defaultDataSource) {
        log.info("正在设置默认数据源, database: {}", defaultDataSource.toString());
        this.defaultDataSource = defaultDataSource;
    }

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
