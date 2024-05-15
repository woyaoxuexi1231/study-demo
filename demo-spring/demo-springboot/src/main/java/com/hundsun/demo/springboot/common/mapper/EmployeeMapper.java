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
     * 保存
     *
     * @param employeeDO do
     */
    void saveOne(EmployeeDO employeeDO);

    /**
     * 查询所有数据,所有字段
     *
     * @return all data
     */
    List<EmployeeDO> selectAllData();

    /**
     * 通过 id 查询 name
     *
     * @param employeeNumber id
     * @return rsp
     */
    String selectLastNameById(@Param(value = "id") Long employeeNumber);
}
