package org.hulei.springboot.thymeleaf.mapper;

import org.hulei.springboot.thymeleaf.model.pojo.EmployeeDO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.mapper
 * @className: EmployeMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:27
 */

@Repository
public interface EmployeeMapper extends BaseMapper<EmployeeDO>, ConditionMapper<EmployeeDO>, MySqlMapper<EmployeeDO> {

}
