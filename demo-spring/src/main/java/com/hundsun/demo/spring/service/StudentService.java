package com.hundsun.demo.spring.service;

import com.hundsun.demo.spring.model.pojo.Dept;
import com.hundsun.demo.spring.model.pojo.Student;

import java.util.List;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-13 16:03
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

public interface StudentService {

    /**
     *
     * @return
     */
    List<Student> getAllStudent();

    /**
     *
     * @return
     */
    List<Dept> getAllDept();
}
