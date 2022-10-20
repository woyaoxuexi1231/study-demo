package com.hundsun.demo.spring.model.pojo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.model.pojo
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-10-19 14:01
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Data
public class School {

    List<Teacher> teachers;

    Set<Student> students;
}
