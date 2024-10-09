package org.hulei.commom.core.model.pojo;

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

    @TableId(value = "customerNumber", type = IdType.AUTO)
    private Integer customernumber;
    @TableField(value = "customerName")
    private String customername;
    @TableField(value = "contactlastname")
    private String contactlastname;
    @TableField(value = "contactfirstname")
    private String contactfirstname;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "addressline1")
    private String addressline1;
    @TableField(value = "addressline2")
    private String addressline2;
    @TableField(value = "city")
    private String city;
    @TableField(value = "state")
    private String state;
    @TableField(value = "postalcode")
    private String postalcode;
    @TableField(value = "country")
    private String country;
    @TableField(value = "salesrepemployeenumber")
    private Integer salesrepemployeenumber;
    @TableField(value = "creditlimit")
    private BigDecimal creditlimit;

}
