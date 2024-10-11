package org.hulei.keeping.server.spring.cache;

import org.hulei.common.mapper.entity.req.PageQryReqDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hulei
 * @since 2024/9/11 10:24
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeQryReq extends PageQryReqDTO {
    private Long employeeNumber;
}
