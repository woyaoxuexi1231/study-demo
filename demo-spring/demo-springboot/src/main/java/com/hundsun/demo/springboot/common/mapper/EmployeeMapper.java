package com.hundsun.demo.springboot.common.mapper;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.mapper
 * @className: EmployeMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:27
 */

public interface EmployeeMapper extends BaseMapper<EmployeeDO>, ConditionMapper<EmployeeDO>, MySqlMapper<EmployeeDO> {
    /**
     * mybatis 使用 $符号
     */
    void saveOne(EmployeeDO employeeDO);

    List<EmployeeDO> selectAll2();

    String selectLastNameById(@Param(value = "id") Long employeeNumber);
}
