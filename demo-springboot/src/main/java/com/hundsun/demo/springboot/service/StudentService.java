package com.hundsun.demo.springboot.service;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-09-23 15:45
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface StudentService {

    /**
     * 插入时执行默认事务 -- REQUIRED
     * 当前不存在事务则新建,存在则加入
     */
    void insertRequired();
}
