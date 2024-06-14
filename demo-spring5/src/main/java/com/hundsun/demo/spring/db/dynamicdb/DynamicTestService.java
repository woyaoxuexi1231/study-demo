package com.hundsun.demo.spring.db.dynamicdb;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.db.dynamicdb
 * @className: DynamicTestService
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/14 0:00
 */

public interface DynamicTestService {
    /**
     * Spring 多数据源小 demo
     *
     * @param dynamicDataSourceType dataSourceType
     */
    void multipleDataSource(DynamicDataSourceType dynamicDataSourceType);

    /**
     * Spring 多数据源 + 事务
     *
     * @param dynamicDataSourceType dataSourceType
     */
    void multipleDataSourceTransaction(DynamicDataSourceType dynamicDataSourceType);
}
