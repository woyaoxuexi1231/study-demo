package com.hundsun.demo.springboot.service.serviceimpl;

import com.hundsun.demo.springboot.mapper.TeacherMapper;
import com.hundsun.demo.springboot.model.domian.TeacherDO;
import com.hundsun.demo.springboot.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service.serviceimpl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-09-23 15:47
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    TeacherMapper teacherMapper;

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void insertNever() {
        TeacherDO teacherDO = new TeacherDO();
        teacherDO.setName("2");
        teacherDO.setAge(2);
        teacherMapper.insertSelective(teacherDO);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void insertMandatory() {

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void insertSupports() {
        TeacherDO teacherDO = new TeacherDO();
        teacherDO.setName("3");
        teacherDO.setAge(3);
        teacherMapper.insertSelective(teacherDO);
        int a = 1 / 0;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void insertNotSupports() {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertRequiresNew() {

    }
}
