package com.hundsun.demo.springboot;

import com.hundsun.demo.springboot.service.serviceimpl.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.controller
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-25 20:20
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@SpringBootApplication
@MapperScan("com.hundsun.demo.springboot.mapper")
@Slf4j
public class SpringbootDemoApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringbootDemoApplication.class);
        StudentServiceImpl studentService = applicationContext.getBean(StudentServiceImpl.class);
        log.info("启动完成");
    }
}
