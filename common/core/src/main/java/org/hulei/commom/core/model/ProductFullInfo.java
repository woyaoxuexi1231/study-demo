package org.hulei.commom.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.commom.core.model.pojo.ProductlinesDO;
import org.hulei.commom.core.model.pojo.ProductsDO;

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
