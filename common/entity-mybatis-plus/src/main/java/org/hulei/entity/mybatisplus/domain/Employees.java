package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "test_employees")
public class Employees {

    @TableId(value = "employee_number", type = IdType.AUTO)
    private Long employeeNumber;

    @TableField(value = "last_name")
    private String lastName;

    @TableField(value = "first_name")
    private String firstName;

    @TableField(value = "extension")
    private String extension;

    @TableField(value = "email")
    private String email;

    @TableField(value = "office_code")
    private String officeCode;

    @TableField(value = "reports_to")
    private Integer reportsTo;

    @TableField(value = "job_title")
    private String jobTitle;

    @TableField(value = "last_update_time")
    private LocalDateTime lastUpdateTime;
}