package org.hulei.springboot.mybatisplus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hulei.entity.mybatisplus.domain.OrderDetails;
import org.hulei.entity.mybatisplus.domain.Orders;

import java.util.List;

/**
 * @author hulei
 * @since 2024/9/26 20:26
 */

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderFullInfo extends Orders {

    List<OrderDetails> details;
}
