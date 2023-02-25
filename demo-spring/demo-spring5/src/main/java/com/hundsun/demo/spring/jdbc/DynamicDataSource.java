package com.hundsun.demo.spring.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.jdbc
 * @className: ThreadLocalVariableRountingDataSource
 * @description:
 * @author: h1123
 * @createDate: 2023/2/18 16:20
 */

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceTypeManager.get();
    }
}
