package com.hundsun.demo.springboot.model.domian;

import lombok.Data;

import javax.persistence.Table;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.model
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-08-01 14:14
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Data
@Table(name = "teacher")
public class TeacherDO {

    String name;

    Integer age;
}
