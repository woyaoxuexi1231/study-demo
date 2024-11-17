package org.hulei.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.hulei.entity.jpa.pojo.Customer;

import java.util.List;

/**
 * @author hulei
 * @since 2024/11/16 21:34
 */

// @Mapper
public interface CustomerMapper {

    @Results({
            @Result(property = "id", column = "customer_number"),
            @Result(property = "customerName", column = "customer_name"),
            @Result(property = "contactLastName", column = "contact_last_name"),
            @Result(property = "contactFirstName", column = "contact_first_name"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "addressLine1", column = "address_line1"),
            @Result(property = "addressLine2", column = "address_line2"),
            @Result(property = "city", column = "city"),
            @Result(property = "state", column = "state"),
            @Result(property = "postalCode", column = "postal_code"),
            @Result(property = "country", column = "country"),
            @Result(property = "salesRepEmployeeNumber", column = "sales_rep_employee_number"),
            @Result(property = "creditLimit", column = "credit_limit")
    })
    @Select("select * from customers")
    List<Customer> selectAll();

    @Update("update customers set phone = #{phone} where customer_number = #{id} ")
    int updateOne(Customer customer);
}
