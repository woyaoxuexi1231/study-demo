package com.hundsun.demo.springboot.model.domian;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.model.domian
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-09-23 14:49
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Table(name = "student")
@Data
@Builder
public class StudentDO {

    String name;

    Integer age;
}
