package com.hundsun.demo.springboot.tkmybatis;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hulei42031
 * @since 2024-03-29 9:58
 */

@RestController
@RequestMapping("/tkmybatis")
public class TkMybatisController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/print")
    public void print() {
        List<EmployeeDO> employeeDOS = employeeMapper.selectAll();
        EmployeeDO employeeDO = employeeDOS.get(0);
    }
}
