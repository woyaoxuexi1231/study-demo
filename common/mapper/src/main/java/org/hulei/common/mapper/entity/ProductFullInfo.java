package org.hulei.common.mapper.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.common.mapper.entity.pojo.ProductlinesDO;
import org.hulei.common.mapper.entity.pojo.ProductsDO;

/**
 * @author hulei
 * @since 2024/9/26 23:17
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductFullInfo extends ProductsDO {
    /**
     * 产品线信息
     */
    ProductlinesDO productlineInfo;
}
