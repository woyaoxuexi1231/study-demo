package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "test_offices")
public class Offices {

    @TableId(value = "office_code", type = IdType.AUTO)
    private String officeCode;

    @TableField(value = "city")
    private String city;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "address_line1")
    private String addressLine1;

    @TableField(value = "address_line2")
    private String addressLine2;

    @TableField(value = "`state`")
    private String state;

    @TableField(value = "country")
    private String country;

    @TableField(value = "postal_code")
    private String postalCode;

    @TableField(value = "territory")
    private String territory;
}