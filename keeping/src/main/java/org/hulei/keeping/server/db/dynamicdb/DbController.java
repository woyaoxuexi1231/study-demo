package org.hulei.keeping.server.db.dynamicdb;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import org.hulei.keeping.server.common.mapper.EmployeeMapper;
import org.hulei.keeping.server.db.dynamicdb.config.coding.DataSourceTag;
import org.hulei.keeping.server.db.dynamicdb.core.DataSourceToggleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: DbController
 * @description:
 * @author: h1123
 * @createDate: 2023/11/4 14:47
 */

@RestController
@RequestMapping("/db")
public class DbController {

    @Autowired
    DynamicDBService dynamicdbService;

    @Resource
    EmployeeMapper employeeMapper;

    /**
     * 双数据源不使用分布式事务如何保证事务
     */
    @GetMapping("/multiDataSourceSingleTransaction")
    public ResultDTO<?> multiDataSourceSingleTransaction() {
        return dynamicdbService.multiDataSourceSingleTransaction();
    }

    /**
     *
     */
    @GetMapping("/transactionInvalidation")
    public void transactionInvalidation() {
        DataSourceToggleUtil.set(DataSourceTag.SECOND.getTag());
        dynamicdbService.transactionInvalidation();
    }

    @Autowired
    DynamicDBServiceImpl dynamicDBServiceImpl;

    @GetMapping("/select")
    public void select(){
        DataSourceToggleUtil.set("second");
        dynamicDBServiceImpl.select();
    }
}
