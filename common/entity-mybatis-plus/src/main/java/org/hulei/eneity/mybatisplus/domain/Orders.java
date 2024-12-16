package org.hulei.eneity.mybatisplus.domain;

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
@TableName(value = "orders")
public class Orders {

    @TableId(value = "order_number", type = IdType.AUTO)
    private Integer orderNumber;

    @TableField(value = "order_date")
    private Date orderDate;

    @TableField(value = "required_date")
    private Date requiredDate;

    @TableField(value = "shipped_date")
    private Date shippedDate;

    @TableField(value = "`status`")
    private String status;

    @TableField(value = "comments")
    private String comments;

    @TableField(value = "customer_number")
    private Integer customerNumber;
}