package org.hulei.eneity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "customers")
public class Customers {

    @TableId(value = "customer_number", type = IdType.AUTO)
    private Integer customerNumber;

    @TableField(value = "customer_name")
    private String customerName;

    @TableField(value = "contact_last_name")
    private String contactLastName;

    @TableField(value = "contact_first_name")
    private String contactFirstName;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "address_line1")
    private String addressLine1;

    @TableField(value = "address_line2")
    private String addressLine2;

    @TableField(value = "city")
    private String city;

    @TableField(value = "`state`")
    private String state;

    @TableField(value = "postal_code")
    private String postalCode;

    @TableField(value = "country")
    private String country;

    @TableField(value = "sales_rep_employee_number")
    private Integer salesRepEmployeeNumber;

    @TableField(value = "credit_limit")
    private BigDecimal creditLimit;
}