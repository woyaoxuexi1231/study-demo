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
@TableName(value = "product_info")
public class ProductInfo {

    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    @TableField(value = "product_name")
    private String productName;

    @TableField(value = "category")
    private String category;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "description")
    private String description;
}