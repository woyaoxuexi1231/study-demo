package com.hundsun.demo.springboot.model.domian;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.model.pojo
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-13 16:26
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Table(name = "DEPT")
@Data
public class DeptDO {

    /**
     *
     */
    @Column(name = "DEPTNO")
    private Integer DEPTNO;

    /**
     *
     */
    @Column(name = "DNAME")
    private String DNAME;

    /**
     *
     */
    @Column(name = "LOC")
    private String LOC;
}
