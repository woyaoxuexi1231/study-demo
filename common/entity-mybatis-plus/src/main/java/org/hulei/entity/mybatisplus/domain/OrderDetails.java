package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_details")
public class OrderDetails {

    @TableId(value = "order_number", type = IdType.AUTO)
    private Integer orderNumber;

    @TableField(value = "product_code")
    private String productCode;

    @TableField(value = "quantity_ordered")
    private Integer quantityOrdered;

    @TableField(value = "price_each")
    private BigDecimal priceEach;

    @TableField(value = "order_line_number")
    private Short orderLineNumber;
}