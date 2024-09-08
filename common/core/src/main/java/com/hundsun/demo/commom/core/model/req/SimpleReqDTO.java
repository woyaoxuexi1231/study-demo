package com.hundsun.demo.commom.core.model.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.commom.core.model.req
 * @className: SimpleReqDTO
 * @description:
 * @author: hl
 * @createDate: 2023/5/15 20:05
 */

@Data
public class SimpleReqDTO implements Serializable {

    private String reqString;

}
