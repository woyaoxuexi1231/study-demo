package org.hulei.springboot.mybatisplus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.eneity.mybatisplus.domain.ProductLines;
import org.hulei.eneity.mybatisplus.domain.Products;

/**
 * @author hulei
 * @since 2024/9/26 23:17
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductFullInfo extends Products {
    /**
     * 产品线信息
     */
    ProductLines productlineInfo;
}
