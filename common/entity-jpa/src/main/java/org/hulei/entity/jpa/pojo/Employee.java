package org.hulei.entity.jpa.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "test_employees", schema = "test")
public class Employee implements Serializable {

    @Id
    @Column(name = "employee_number", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 10)
    @NotNull
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 10)
    @NotNull
    @Column(name = "office_code", nullable = false, length = 10)
    private String officeCode;

    @Column(name = "reports_to")
    private Integer reportsTo;

    @Size(max = 50)
    @NotNull
    @Column(name = "job_title", nullable = false, length = 50)
    private String jobTitle;

    @NotNull
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;

}