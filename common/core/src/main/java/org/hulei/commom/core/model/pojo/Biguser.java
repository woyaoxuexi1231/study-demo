package org.hulei.commom.core.model.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Biguser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String userName;

    /**
     * 身份证
     */
    private String ssn;

    /**
     * 名字
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 车牌
     */
    private String plate;

    /**
     * 地址
     */
    private String address;

    /**
     * 楼名
     */
    private String buildingNumber;

    /**
     * 国家
     */
    private String country;

    /**
     * 生日
     */
    private String birth;

    /**
     * 公司
     */
    private String company;

    /**
     * 职位
     */
    private String job;

    /**
     * 信用卡号
     */
    private String cardNumber;

    /**
     * 城市
     */
    private String city;

    /**
     * 星期
     */
    private String week;

    /**
     * 邮件
     */
    private String email;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String paragraphs;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}