package org.hulei.commom.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.hulei.commom.core.model.pojo.EmployeeDO;
import org.hulei.commom.core.model.EmployeeTreeRsp;
import org.hulei.commom.core.model.EmployeeWithCustomersRsp;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 使用collection查询数据
     *
     * @param employeeId
     * @return
     */
    List<EmployeeWithCustomersRsp> getEmployeeWithCustomers(@Param("employeeNumber") Long employeeId);

    /**
     * 使用ResultMap获取数据
     *
     * @return 雇员列表
     */
    List<EmployeeDO> getEmployeeWithResultMap();

    /**
     * $符号与#号的区别
     *
     * @param employeeId id
     * @return 雇员
     */
    EmployeeDO getEmployeeByIdTestInject(Long employeeId);

    /**
     * 获取employee树状结构
     *
     * @return
     */
    List<EmployeeTreeRsp> getEmployeeTree(@Param(value = "employeeNumber") Long employeeNumber);

    List<EmployeeTreeRsp> getEmployeeByReportNumber(@Param("reportsTo") Long employeeNumber);

    List<EmployeeDO> selectTagsTest();

    /**
     * mybatis使用流式查询/游标查询
     *
     * @return
     */
    List<EmployeeDO> mybatisStreamQuery();

    /**
     * 使用ResultHandler来进行流式查询操作结果集的结果
     *
     * @param handler
     */
    void resultSetOpe(ResultHandler<EmployeeDO> handler);
}
