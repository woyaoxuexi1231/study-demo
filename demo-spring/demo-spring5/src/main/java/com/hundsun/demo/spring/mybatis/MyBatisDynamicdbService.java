package com.hundsun.demo.spring.mybatis;

import com.hundsun.demo.spring.db.dynamicdb.DynamicDataSourceType;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatis
 * @className: MyBatisDynamicdbSrvice
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/14 1:03
 */

public interface MyBatisDynamicdbService {
    /**
     * Mybatis + Spring 整合, 事务
     *
     * @param myBatisOperationType  myBatisOperationType
     * @param dynamicDataSourceType dataSourceType
     */
    void mybatisSpringTransaction(MyBatisOperationType myBatisOperationType, DynamicDataSourceType dynamicDataSourceType);
}
