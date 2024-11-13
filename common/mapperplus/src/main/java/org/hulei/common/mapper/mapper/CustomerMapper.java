package org.hulei.common.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.common.mapper.entity.pojo.CustomerDO;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatisplugins
 * @className: CustomerMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 21:51
 */

public interface CustomerMapper extends BaseMapper<CustomerDO> {

    /**
     * get all customers
     *
     * @return CustomerDOs
     */
    List<CustomerDO> selectAll();

    /**
     * 单条更新
     *
     * @param customerDO customerDO
     */
    void updateOne(CustomerDO customerDO);

    List<CustomerDO> selectCustomerByEmployeeId(Long employeeNumber);

    CustomerDO selectByEnmu();
}
