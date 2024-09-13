package org.hulei.keeping.server.redis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.commom.core.model.req.PageQryReqDTO;
import org.hulei.keeping.server.mybatis.mybatisplus.mapper.EmployeeMapperPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/11 15:07
 */

@RequestMapping("/redisCache")
@RestController
public class RedisCacheController {

    @Autowired
    EmployeeMapperPlus employeeMapperPlus;

    @PostMapping(value = "/getEmployees")
    public List<EmployeeDO> getEmployees(@RequestBody PageQryReqDTO req) {
        LambdaQueryWrapper<EmployeeDO> wrapper = new LambdaQueryWrapper<>();
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        return employeeMapperPlus.selectList(wrapper);
    }

}
