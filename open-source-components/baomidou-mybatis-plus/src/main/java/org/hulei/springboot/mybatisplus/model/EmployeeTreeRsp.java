package org.hulei.springboot.mybatisplus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.mybatisplus.domain.Employees;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 21:46
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeTreeRsp extends Employees {

    List<EmployeeTreeRsp> children;
}
