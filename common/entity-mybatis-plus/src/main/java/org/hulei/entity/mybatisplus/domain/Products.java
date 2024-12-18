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
@TableName(value = "products")
public class Products {

    @TableId(value = "product_code", type = IdType.AUTO)
    private String productCode;

    @TableField(value = "product_name")
    private String productName;

    @TableField(value = "product_line")
    private String productLine;

    @TableField(value = "product_scale")
    private String productScale;

    @TableField(value = "product_vendor")
    private String productVendor;

    @TableField(value = "product_description")
    private String productDescription;

    @TableField(value = "quantity_in_stock")
    private Short quantityInStock;

    @TableField(value = "buy_price")
    private BigDecimal buyPrice;

    @TableField(value = "msrp")
    private BigDecimal msrp;
}