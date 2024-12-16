package org.hulei.springdata.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hulei
 * @since 2024/11/17 23:24
 */

@Builder
@Data
@Table(name = "employees")
public class EmployeeDO implements Serializable {

    @Id
    @Column(value = "employee_number")
    private Long employeeNumber;

    @Column(value = "last_name")
    private String lastName;

    @Column(value = "first_name")
    private String firstName;

    @Column(value = "extension")
    private String extension;

    @Column(value = "email")
    private String email;

    @Column(value = "office_code")
    private String officeCode;

    @Column(value = "reports_to")
    private Integer reportsTo;

    @Column(value = "job_title")
    private String jobTitle;

    @Column(value = "last_update_time")
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
