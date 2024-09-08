package com.hundsun.demo.commom.core.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.dubbo.common.api.model.dto
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-06-10 15:55
 */

@Data
public class ResultDTO<E> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 结果
     */
    private E data;

}
