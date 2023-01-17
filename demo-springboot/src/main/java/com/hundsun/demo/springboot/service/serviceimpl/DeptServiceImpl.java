package com.hundsun.demo.springboot.service.serviceimpl;

import com.hundsun.demo.springboot.mapper.oracle.DeptMapper;
import com.hundsun.demo.springboot.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.service.serviceimpl
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-01-16 10:51
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */
@Service
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Resource
    DeptMapper deptMapper;

    @Override
    public void getAllDept() {
        log.info(deptMapper.selectAll().toString());
    }
}
