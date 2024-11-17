package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "employees", schema = "test")
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