package org.hulei.commom.core.model.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class OrdersDO {

    private Integer ordernumber;

    private Date orderdate;

    private Date requireddate;

    private Date shippeddate;

    private String status;

    private String comments;

    private Integer customernumber;
}