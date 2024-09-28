package org.hulei.commom.core.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "product_info")
@Data
public class ProductInfoDO implements Serializable {
    /**
     * 产品序号
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;
    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    private String productName;
    /**
     * 类别
     */
    @TableField(value = "category")
    private String category;
    /**
     * 价格
     */
    @TableField(value = "price")
    private BigDecimal price;
    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;
}
