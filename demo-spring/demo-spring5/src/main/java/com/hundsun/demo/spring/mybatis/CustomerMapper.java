package com.hundsun.demo.spring.mybatis;

import com.hundsun.demo.spring.model.pojo.CustomerDO;

import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.mybatisplugins
 * @className: CustomerMapper
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 21:51
 */

public interface CustomerMapper {

    /**
     * get all customers
     *
     * @return CustomerDOs
     */
    List<CustomerDO> selectAll();
}
