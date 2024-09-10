package org.hulei.keeping.server.common.model.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hulei
 * @since 2024/8/12 15:07
 */

@Data
public class EmployeeModReqDTO implements Serializable {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
