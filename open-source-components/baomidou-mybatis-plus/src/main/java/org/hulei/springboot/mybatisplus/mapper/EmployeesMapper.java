package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.hulei.eneity.mybatisplus.domain.Employees;
import org.hulei.springboot.mybatisplus.model.EmployeeTreeRsp;
import org.hulei.springboot.mybatisplus.model.EmployeeWithCustomersRsp;

import java.util.List;

public interface EmployeesMapper extends BaseMapper<Employees> {


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
    List<Employees> getEmployeeWithResultMap();

    /**
     * $符号与#号的区别
     *
     * @param employeeId id
     * @return 雇员
     */
    Employees getEmployeeByIdTestInject(Long employeeId);

    /**
     * 获取employee树状结构
     *
     * @return
     */
    List<EmployeeTreeRsp> getEmployeeTree(@Param(value = "employeeNumber") Long employeeNumber);

    List<EmployeeTreeRsp> getEmployeeByReportNumber(@Param("reportsTo") Long employeeNumber);

    List<Employees> selectTagsTest();

    /**
     * mybatis使用流式查询/游标查询
     *
     * @return
     */
    List<Employees> mybatisStreamQuery();

    /**
     * 使用ResultHandler来进行流式查询操作结果集的结果
     *
     * @param handler
     */
    void resultSetOpe(ResultHandler<Employees> handler);
}