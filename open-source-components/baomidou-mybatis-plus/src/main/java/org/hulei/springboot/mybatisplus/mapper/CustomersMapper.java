package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.eneity.mybatisplus.domain.Customers;

import java.util.List;

public interface CustomersMapper extends BaseMapper<Customers> {
    /**
     * get all customers
     *
     * @return CustomerDOs
     */
    List<Customers> selectAll();

    /**
     * 单条更新
     *
     * @param customerDO customerDO
     */
    void updateOne(Customers customerDO);

    List<Customers> selectCustomerByEmployeeId(Long employeeNumber);

    Customers selectByEnmu();
}