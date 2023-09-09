package com.hundsun.demo.springboot.model.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
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
public class EmployeeDO {

    /**
     *
     */
    @Id
    @Column(name = "employeeNumber")
    private Long employeeNumber;
    /**
     *
     */
    @Column(name = "lastName")
    private String lastName;
    /**
     *
     */
    @Column(name = "firstName")
    private String firstName;
    /**
     *
     */
    @Column(name = "extension")
    private String extension;
    /**
     *
     */
    @Column(name = "email")
    private String email;
    /**
     *
     */
    @Column(name = "officeCode")
    private String officeCode;
    /**
     *
     */
    @Column(name = "reportsTo")
    private Integer reportsTo;
    /**
     *
     */
    @Column(name = "jobTitle")
    private String jobTitle;
}
