package org.hulei.springboot.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeDO implements Serializable {

    private Long employeeNumber;

    private String lastName;

    private String firstName;

    private String extension;

    private String email;

    private String officeCode;

    private Integer reportsTo;

    private String jobTitle;

    private LocalDateTime lastUpdateTime;
}
