package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.model.pojo.Dept;
import com.hundsun.demo.spring.model.pojo.Student;
import com.hundsun.demo.spring.service.StudentService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-13 16:01
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public class StudentServiceImpl implements StudentService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<Student> getAllStudent() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate1");
        return jdbcTemplate.query("select * from student", new BeanPropertyRowMapper<>(Student.class));
    }

    @Override
    public List<Dept> getAllDept() {
        JdbcTemplate jdbcTemplate = (JdbcTemplate) applicationContext.getBean("jdbcTemplate2");
        return jdbcTemplate.query("select * from DEPT", new BeanPropertyRowMapper<>(Dept.class));
    }
}
