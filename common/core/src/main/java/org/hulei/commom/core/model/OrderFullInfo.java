package org.hulei.commom.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.commom.core.model.pojo.OrderdetailsDO;
import org.hulei.commom.core.model.pojo.OrdersDO;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 20:26
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderFullInfo extends OrdersDO {

    List<OrderdetailsDO> details;
}
