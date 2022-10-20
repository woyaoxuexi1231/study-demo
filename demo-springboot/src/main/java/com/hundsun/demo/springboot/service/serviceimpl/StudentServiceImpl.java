package com.hundsun.demo.springboot.service.serviceimpl;

import com.hundsun.demo.springboot.mapper.StudentMapper;
import com.hundsun.demo.springboot.model.domian.StudentDO;
import com.hundsun.demo.springboot.service.StudentService;
import com.hundsun.demo.springboot.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service.serviceimpl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-09-23 15:46
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    StudentMapper studentMapper;

    @Autowired
    TeacherService teacherService;

    @Override
    @Transactional
    public void insertRequired() {

        StudentDO studentDO = StudentDO.builder().name("张三").age(28).build();
        studentMapper.insertSelective(studentDO);

        teacherService.insertNever();
        teacherService.insertSupports();
    }
}
