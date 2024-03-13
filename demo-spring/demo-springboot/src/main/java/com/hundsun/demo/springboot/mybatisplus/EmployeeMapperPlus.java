package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hundsun.demo.springboot.common.model.EmployeeDO;
import org.springframework.stereotype.Repository;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.mybatisplus
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-12-11 19:41
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

@Repository
public interface EmployeeMapperPlus extends BaseMapper<EmployeeDO> {
}
