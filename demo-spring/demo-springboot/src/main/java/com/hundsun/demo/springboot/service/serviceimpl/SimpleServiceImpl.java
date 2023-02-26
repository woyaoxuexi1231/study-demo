package com.hundsun.demo.springboot.service.serviceimpl;

import com.hundsun.demo.springboot.annotation.TargetDataSource;
import com.hundsun.demo.springboot.dynamic.DynamicDataSourceType;
import com.hundsun.demo.springboot.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.service.serviceimpl
 * @className: SimpleServiceImpl
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:32
 */

@Service
public class SimpleServiceImpl implements SimpleService {

    /**
     * tkmybatis mapper
     */
    @Resource
    EmployeeMapper employeeMapper;

    /**
     *
     */
    @Autowired
    SimpleService simpleService;

    /**
     *
     */
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;


    @Override
    @Transactional
    @TargetDataSource(dataSourceType = DynamicDataSourceType.MASTER)
    public void springTransaction() {

        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(1002);
        employeeDO.setLastName("Murph2");
        employeeMapper.updateByPrimaryKeySelective(employeeDO);

    }

    @Override
    @Transactional
    @TargetDataSource(dataSourceType = DynamicDataSourceType.SECOND)
    public void springRedis() {

        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeNumber(1002);
        employeeDO.setLastName("Murph2");
        employeeMapper.updateByPrimaryKeySelective(employeeDO);

    }
}
