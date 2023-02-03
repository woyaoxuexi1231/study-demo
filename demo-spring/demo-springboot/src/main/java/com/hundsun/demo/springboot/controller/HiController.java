package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.springboot.service.DeptService;
import com.hundsun.demo.springboot.service.StudentService;
import com.hundsun.demo.springboot.service.serviceimpl.HiServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-25 20:19
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@RequestMapping("/test")
@RestController
@Slf4j
public class HiController implements ApplicationContextAware {

    /**
     *
     */
    @Autowired
    StudentService studentService;

    /**
     *
     */
    @Autowired
    DeptService deptService;

    /**
     *
     */
    @Autowired
    HiServiceImpl hiServiceImpl;

    /**
     *
     */
    private ApplicationContext applicationContext;

    // @Resource
    // StudentMapper studentMapper;

    @RequestMapping("/say")
    public void say() {
        hiServiceImpl.sayHi();
    }

    @RequestMapping("/testTransaction")
    public void testTransaction() {
        studentService.insertRequired();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/getAllStudent")
    public void getAllStudent() {
        studentService.getAllStudent();
    }

    @RequestMapping("/getAllDept")
    public void getAllDept() {
        deptService.getAllDept();
    }

    // @RequestMapping("/selectAll")
    // public void selectAll(){
    //     PageHelper.startPage(1,10);
    //     PageInfo<StudentDO> studentDOS = new PageInfo<>(studentMapper.selectAll());
    //     System.out.println(studentDOS);
    // }
    // // todo 这里显然是注入不了的
    // @Autowired(required = false)
    // NoImplInterface noImplInterface;
    //
    // @Autowired
    // DataSource dataSource;

    // @RequestMapping("/say2")
    // public void say2() throws SQLException {
    //     System.out.println(dataSource.toString());
    //     System.out.println(dataSource.getConnection().getMetaData().getURL());
    // }
}
