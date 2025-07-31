package org.hulei.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.commom.core.model.req
 * @className: SimpleReqDTO
 * @description:
 * @author: hl
 * @createDate: 2023/5/15 20:05
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleReqDTO implements Serializable {

    private String reqString;

}
