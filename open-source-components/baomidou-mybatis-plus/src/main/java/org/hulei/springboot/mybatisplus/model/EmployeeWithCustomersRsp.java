package org.hulei.springboot.mybatisplus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.mybatisplus.domain.Customers;
import org.hulei.entity.mybatisplus.domain.Employees;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 15:53
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeWithCustomersRsp extends Employees {

    List<Customers> customers;
}
