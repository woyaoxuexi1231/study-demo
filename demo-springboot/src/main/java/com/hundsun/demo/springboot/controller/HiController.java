package com.hundsun.demo.springboot.controller;

import com.hundsun.demo.springboot.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HiController {

    @Autowired
    StudentService studentService;

    @RequestMapping("/say")
    public void say() {
        System.out.println("test");
    }

    @RequestMapping("/testTransaction")
    public void testTransaction() {
        studentService.insertRequired();
    }

}
