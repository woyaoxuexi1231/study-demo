package org.hulei.springboot.js.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;
import org.hulei.common.mapper.entity.req.PageQryReqDTO;
import org.hulei.common.mapper.mapper.EmployeeMapperPlus;
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
    EmployeeMapperPlus employeeMapperPlus;

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting(@RequestParam(name = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok("Hello, " + name + "!");
    }

    @PostMapping("/getUsers")
    public PageInfo<EmployeeDO> getUsers(@Valid @RequestBody PageQryReqDTO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return new PageInfo<>(employeeMapperPlus.selectList(Wrappers.emptyWrapper()));
    }
}
