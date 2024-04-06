package org.hulei.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hulei.springboot.mapper.EmployeeMapper;
import org.hulei.springboot.model.pojo.EmployeeDO;
import org.hulei.springboot.model.req.EmployeeQryReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: org.hulei.springboot.controller
 * @className: EmployeeController
 * @description:
 * @author: woaixuexi
 * @createDate: 2024/3/10 19:06
 */

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    EmployeeMapper employeeMapper;

    @PostMapping(value = "/getEmployees")
    public PageInfo<EmployeeDO> getEmployees(@Valid @RequestBody EmployeeQryReqDTO req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return new PageInfo<>(employeeMapper.selectAll());
    }

    @PostMapping("/change")
    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        map.put("response-tag", new Date());
        return map;
    }
}
