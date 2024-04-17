package com.hundsun.demo.springboot.tkmybatis;

import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei42031
 * @since 2024-03-29 9:58
 */

@Slf4j
@RestController
@RequestMapping("/tkmybatis")
public class TkMybatisController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/print")
    public void print(Long id) {
        // 数组的临界情况为数组是空数组,数组对象是不为空的
        // List<EmployeeDO> employeeDOS = employeeMapper.selectAll();
        // EmployeeDO employeeDO = employeeDOS.get(0);
        // 基本类型的临界值情况是直接为null
        String s = employeeMapper.selectLastNameById(id);
        System.out.println(s);
    }
}
