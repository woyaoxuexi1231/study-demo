package com.hundsun.demo.spring.service;

import com.hundsun.demo.spring.jdbc.DynamicDataSourceType;
import com.hundsun.demo.spring.model.pojo.CustomerDO;
import com.hundsun.demo.spring.mybatis.MyBatisOperationType;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-13 16:03
 */

public interface YiibaidbService {

    /**
     * 通过 spring JdbcTemplate 访问数据库, 获取数据
     *
     * @return data
     */
    List<CustomerDO> jdbcTemplateQuery();

    /**
     * JdbcTemplate + Spring 事务管理小 demo
     */
    void jdbcTemplateUpdate();

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

    /**
     * Mybatis + Spring 整合, 事务
     *
     * @param myBatisOperationType myBatisOperationType
     * @param dynamicDataSourceType       dataSourceType
     */
    void mybatisSpringTransaction(MyBatisOperationType myBatisOperationType, DynamicDataSourceType dynamicDataSourceType);
}
