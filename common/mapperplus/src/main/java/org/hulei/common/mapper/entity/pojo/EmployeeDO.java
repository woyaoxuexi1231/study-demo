package org.hulei.common.mapper.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

@Builder
@Data
@Table(name = "employees")
@TableName(value = "employees")
public class EmployeeDO implements Serializable {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Mysql")
    @Column(name = "employee_number")
    @TableId(value = "employee_number")
    private Long employeeNumber;
    /**
     *
     */
    @Column(name = "last_name")
    @TableField(value = "last_name")
    private String lastName;
    /**
     *
     */
    @Column(name = "first_name")
    @TableField(value = "first_name")
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
    @Column(name = "office_code")
    @TableField(value = "office_code")
    private String officeCode;
    /**
     *
     */
    @Column(name = "reports_to")
    @TableField(value = "reports_to")
    private Integer reportsTo;
    /**
     *
     */
    @Column(name = "job_title")
    @TableField(value = "job_title")
    private String jobTitle;

    @Column(name = "last_update_time")
    @TableField(value = "last_update_time")
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
