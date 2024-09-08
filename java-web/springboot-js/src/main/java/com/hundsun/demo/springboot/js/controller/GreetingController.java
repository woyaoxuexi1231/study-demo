package com.hundsun.demo.springboot.js.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.demo.springboot.js.mapper.EmployeeMapper;
import com.hundsun.demo.springboot.js.model.pojo.EmployeeDO;
import com.hundsun.demo.springboot.js.model.req.EmployeeQryReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.js.controller
 * @className: GreetingController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 13:48
 */

@RestController
public class GreetingController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting(@RequestParam(name = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok("Hello, " + name + "!");
    }

    @PostMapping("/getUsers")
    public PageInfo<EmployeeDO> getUsers(@Valid @RequestBody EmployeeQryReqDTO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return new PageInfo<>(employeeMapper.selectAll());
    }
}
