package com.hundsun.demo.commom.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
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
    @Id
    @Column(name = "employeeNumber")
    @TableField(value = "employeeNumber")
    private Long employeeNumber;
    /**
     *
     */
    @Column(name = "lastName")
    @TableField(value = "lastName")
    private String lastName;
    /**
     *
     */
    @Column(name = "firstName")
    @TableField(value = "firstName")
    private String firstName;
    /**
     *
     */
    @Column(name = "extension")
    @TableField(value = "extension")
    private String extension;
    /**
     *
     */
    @Column(name = "email")
    @TableField(value = "email")
    private String email;
    /**
     *
     */
    @Column(name = "officeCode")
    @TableField(value = "officeCode")
    private String officeCode;
    /**
     *
     */
    @Column(name = "reportsTo")
    @TableField(value = "reportsTo")
    private Integer reportsTo;
    /**
     *
     */
    @Column(name = "jobTitle")
    @TableField(value = "jobTitle")
    private String jobTitle;
}
