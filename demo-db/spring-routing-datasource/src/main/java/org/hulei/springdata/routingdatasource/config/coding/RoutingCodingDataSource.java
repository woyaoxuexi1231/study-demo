package org.hulei.springdata.routingdatasource.config.coding;

import lombok.RequiredArgsConstructor;
import org.hulei.springdata.routingdatasource.core.DataSourceToggleUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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

@RequiredArgsConstructor
public class RoutingCodingDataSource extends AbstractRoutingDataSource {

    private final DataSource dataSourceFirst;
    private final DataSource dataSourceSecond;

    @Override
    public void afterPropertiesSet() {

        // 设置标志与多数据源的关系
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceTag.FIRST.tag, dataSourceFirst);
        targetDataSource.put(DataSourceTag.SECOND.tag, dataSourceSecond);
        super.setTargetDataSources(targetDataSource);

        // 执行 AbstractRoutingDataSource 的赋值操作
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceToggleUtil.get();
    }
}
