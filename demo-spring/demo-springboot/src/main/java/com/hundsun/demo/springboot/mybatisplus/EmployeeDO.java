package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Table;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.model.pojo
 * @className: EmployeDO
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:24
 */

@Data
@Table(name = "employees")
@TableName(value = "employees")
public class EmployeeDO {

    /**
     *
     */
    @TableId(value = "employeeNumber")
    private Long employeeNumber;
    /**
     *
     */
    @TableField(value = "lastName")
    private String lastName;
    /**
     *
     */
    @TableField(value = "firstName")
    private String firstName;
    /**
     *
     */
    @TableField(value = "extension")
    private String extension;
    /**
     *
     */
    @TableField(value = "email")
    private String email;
    /**
     *
     */
    @TableField(value = "officeCode")
    private String officeCode;
    /**
     *
     */
    @TableField(value = "reportsTo")
    private Integer reportsTo;
    /**
     *
     */
    @TableField(value = "jobTitle")
    private String jobTitle;
}
