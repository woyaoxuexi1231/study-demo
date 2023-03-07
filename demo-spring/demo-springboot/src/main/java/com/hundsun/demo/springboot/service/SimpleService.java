package com.hundsun.demo.springboot.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service
 * @className: SimpleService
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:32
 */

public interface SimpleService {

    /**
     * 双数据源不使用分布式事务如何保证事务
     */
    ResultDTO<?> multiDataSourceSingleTransaction();

    /**
     * 主数据源的更改操作
     */
    void copySlaveToMaster(List<EmployeeDO> employeeDOS, Semaphore masterSemaphore, Semaphore slaveSemaphore, AtomicBoolean isFinished);

    /**
     * 从数据源的更改操作
     */
    void selectFromSlave(List<EmployeeDO> employeeDOS, Semaphore masterSemaphore, Semaphore slaveSemaphore, AtomicBoolean isFinished);

    void mysqlSelect();

    void mysqlUpdate();

    /**
     * mybatis 的两种填值方式
     */
    void mybatis();
}
