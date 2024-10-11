package org.hulei.springdata.jpa.pojo;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.model.pojo
 * @className: EmployeDO
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:24
 */

// entity注解标识当前类是一个持久化实体,这个对象的状态应该被存储到数据库中,如果没有指定@Table注解,那么类名将作为表名(类名转下划线)
@Entity
@Builder
@Data
@Table(name = "employees")
public class EmployeeDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Mysql")
    // 标识当前数据库的列名是什么,如果数据库的列名是大驼峰,这里需要写成小驼峰才能完成正常的映射
    @Column(name = "employee_number")
    private Long employeeNumber;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "email")
    private String email;

    @Column(name = "office_code")
    private String officeCode;

    @Column(name = "reports_to")
    private Integer reportsTo;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    public EmployeeDO(Long employeeNumber, String lastName, String firstName, String extension, String email, String officeCode, Integer reportsTo, String jobTitle, LocalDateTime lastUpdateTime) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.extension = extension;
        this.email = email;
        this.officeCode = officeCode;
        this.reportsTo = reportsTo;
        this.jobTitle = jobTitle;
        this.lastUpdateTime = lastUpdateTime;
    }

    public EmployeeDO() {
    }
}
