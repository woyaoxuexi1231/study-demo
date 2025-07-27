package org.hulei.mybatis.spring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.jpa.pojo.Product;
import org.hulei.entity.jpa.pojo.ProductLine;

/**
 * @author hulei
 * @since 2024/9/26 23:17
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductFullInfo extends Product {
    /**
     * 产品线信息
     */
    ProductLine productLine;
}
