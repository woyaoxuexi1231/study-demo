package org.hulei.common.mapper.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.model.pojo
 * @className: CustomerDO
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 0:31
 */

@TableName(value = "customers")
@Data
public class CustomerDO {

    @TableId(value = "customer_number", type = IdType.AUTO)
    private Integer customernumber;

    @TableField(value = "customer_name")
    private String customername;

    @TableField(value = "contact_last_name")
    private String contactlastname;

    @TableField(value = "contact_first_name")
    private String contactfirstname;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "address_line1")
    private String addressline1;

    @TableField(value = "address_line2")
    private String addressline2;

    @TableField(value = "city")
    private String city;

    @TableField(value = "state")
    private String state;

    @TableField(value = "postal_code")
    private String postalcode;

    @TableField(value = "country")
    private String country;

    @TableField(value = "sales_rep_employee_number")
    private Integer salesrepemployeenumber;

    @TableField(value = "credit_limit")
    private BigDecimal creditlimit;

}
