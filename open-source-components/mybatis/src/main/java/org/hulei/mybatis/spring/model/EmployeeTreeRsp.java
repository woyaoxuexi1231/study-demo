package org.hulei.mybatis.spring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.jpa.pojo.Employee;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 21:46
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeTreeRsp extends Employee {

    List<EmployeeTreeRsp> children;
}
