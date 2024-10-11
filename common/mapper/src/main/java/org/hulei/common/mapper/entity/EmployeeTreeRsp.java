package org.hulei.common.mapper.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.common.mapper.entity.pojo.EmployeeDO;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 21:46
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeTreeRsp extends EmployeeDO {

    List<EmployeeTreeRsp> children;
}
