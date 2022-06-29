package com.hundsun.demo.dubbo.common.api.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.model.dto
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 15:55
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Data
public class ResultDTO<E> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 结果
     */
    private E data;

}
