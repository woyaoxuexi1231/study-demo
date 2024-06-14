package com.hundsun.demo.springboot.db.dynamicdb;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.dto.ResultDTO;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.dynamicdb
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-03-13 20:23
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public interface DynamicdbService {
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

    /**
     * 测试事务失效
     */
    void transactionInvalidation();
}
