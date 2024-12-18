package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "big_user")
public class BigUser {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 身份证
     */
    @TableField(value = "ssn")
    private String ssn;

    /**
     * 名字
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 车牌
     */
    @TableField(value = "plate")
    private String plate;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 楼名
     */
    @TableField(value = "building_number")
    private String buildingNumber;

    /**
     * 国家
     */
    @TableField(value = "country")
    private String country;

    /**
     * 生日
     */
    @TableField(value = "birth")
    private String birth;

    /**
     * 公司
     */
    @TableField(value = "company")
    private String company;

    /**
     * 职位
     */
    @TableField(value = "job")
    private String job;

    /**
     * 信用卡号
     */
    @TableField(value = "card_number")
    private String cardNumber;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 星期
     */
    @TableField(value = "week")
    private String week;

    /**
     * 邮件
     */
    @TableField(value = "email")
    private String email;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "paragraphs")
    private String paragraphs;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}