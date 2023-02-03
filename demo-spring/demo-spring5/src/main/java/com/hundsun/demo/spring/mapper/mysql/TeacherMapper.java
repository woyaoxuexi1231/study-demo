package com.hundsun.demo.spring.mapper.mysql;

import com.hundsun.demo.spring.model.pojo.TeacherDO;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mapper
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-10-20 11:11
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
// @Mapper
public interface TeacherMapper {

    public TeacherDO selectByName(String name);
}
