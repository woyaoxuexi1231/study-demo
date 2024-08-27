package com.hundsun.demo.springboot.db.dynamicdb.config.coding;

import com.hundsun.demo.springboot.db.dynamicdb.core.DataSourceToggleUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.dynamic
 * @className: DynamicDataSource
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 17:44
 */

public class RoutingCodingDataSource extends AbstractRoutingDataSource {

    @Resource
    private DataSource dataSourceFirst;

    @Resource
    private DataSource dataSourceSecond;

    @Override
    public void afterPropertiesSet() {

        // 设置标志与多数据源的关系
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceTag.MASTER, dataSourceFirst);
        targetDataSource.put(DataSourceTag.SECOND, dataSourceSecond);
        super.setTargetDataSources(targetDataSource);

        // 执行 AbstractRoutingDataSource 的赋值操作
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceToggleUtil.get();
    }
}
