package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.springboot.utils.dynamicdb.core.DynamicDataSourceType;
import com.hundsun.demo.springboot.utils.dynamicdb.core.DynamicDataSourceTypeManager;
import com.hundsun.demo.springboot.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.service.serviceimpl.SimpleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: DbController
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 14:47
 */

@RestController
@RequestMapping("/db")
public class DbController {

    @Autowired
    SimpleServiceImpl simpleServiceImpl;

    @Resource
    EmployeeMapper employeeMapper;

    /**
     * 双数据源不使用分布式事务如何保证事务
     */
    @GetMapping("/multiDataSourceSingleTransaction")
    public ResultDTO<?> multiDataSourceSingleTransaction() {
        return simpleServiceImpl.multiDataSourceSingleTransaction();
    }

    @GetMapping("/mysqlSelect")
    public void mysqlSelect() {
        simpleServiceImpl.mysqlSelect();
    }

    @GetMapping("/mysqlUpdate")
    public void mysqlUpdate() {
        simpleServiceImpl.mysqlUpdate();
    }

    @GetMapping("/mybatis")
    public void mybatis() {
        // simpleService.mybatis();
        EmployeeDO employeeDO = new EmployeeDO();
        // employeeDO.setEmployeeNumber(System.currentTimeMillis());
        employeeDO.setLastName("n");
        employeeDO.setFirstName("a");
        employeeMapper.insertSelective(employeeDO);
    }

    @GetMapping("/testMysqlAutoKey")
    public void testMysqlAutoKey() {
        simpleServiceImpl.testMysqlAutoKey();
    }

    @GetMapping("/transactionInvalidation")
    public void transactionInvalidation() {
        DynamicDataSourceTypeManager.set(DynamicDataSourceType.SECOND);
        simpleServiceImpl.transactionInvalidation();
    }


}
