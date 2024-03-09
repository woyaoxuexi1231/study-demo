package com.hundsun.demo.springboot.utils.mybatisplus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mybatisplus
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-11 20:22
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Service
public class TestServiceImpl implements TestService {

    /**
     * author: hulei42031
     * date: 2023-12-11 19:43
     */
    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    @Transactional
    public void run(String name) {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(1002L);
        employeeDO.setFirstName(name + "service");
        employeeMapperPlus.updateById(employeeDO);
        throw new RuntimeException("error");
    }
}
