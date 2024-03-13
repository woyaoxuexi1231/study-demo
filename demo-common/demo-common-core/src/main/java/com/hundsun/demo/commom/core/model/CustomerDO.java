package com.hundsun.demo.commom.core.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.spring.model.pojo
 * @className: CustomerDO
 * @description:
 * @author: h1123
 * @createDate: 2023/2/7 0:31
 */
@Data
public class CustomerDO {

    /**
     *
     */
    private Integer customernumber;
    /**
     *
     */
    private String customername;
    /**
     *
     */
    private String contactlastname;
    /**
     *
     */
    private String contactfirstname;
    /**
     *
     */
    private String phone;
    /**
     *
     */
    private String addressline1;
    /**
     *
     */
    private String addressline2;
    /**
     *
     */
    private String city;
    /**
     *
     */
    private String state;
    /**
     *
     */
    private String postalcode;
    /**
     *
     */
    private String country;
    /**
     *
     */
    private Integer salesrepemployeenumber;
    /**
     *
     */
    private BigDecimal creditlimit;

}
