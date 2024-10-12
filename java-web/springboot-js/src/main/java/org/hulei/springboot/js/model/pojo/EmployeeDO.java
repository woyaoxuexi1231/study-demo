package org.hulei.springboot.js.model.pojo;

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
public class EmployeeDO {

    /**
     *
     */
    @Id
    @Column(name = "employee_number")
    private Long employeeNumber;
    /**
     *
     */
    @Column(name = "last_name")
    private String lastName;
    /**
     *
     */
    @Column(name = "first_name")
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
    @Column(name = "office_code")
    private String officeCode;
    /**
     *
     */
    @Column(name = "reports_to")
    private Integer reportsTo;
    /**
     *
     */
    @Column(name = "job_title")
    private String jobTitle;
}
