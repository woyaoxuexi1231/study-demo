package org.hulei.common.mapper.entity.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderdetailsDO {

    private Integer ordernumber;

    private String productcode;

    private Integer quantityordered;

    private BigDecimal priceeach;

    private Short orderlinenumber;
}