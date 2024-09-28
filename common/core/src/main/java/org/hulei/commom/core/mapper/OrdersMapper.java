package org.hulei.commom.core.mapper;


import org.hulei.commom.core.model.OrderFullInfo;
import org.hulei.commom.core.model.pojo.OrdersDO;

import java.util.List;

public interface OrdersMapper {

    List<OrderFullInfo> selectOrderFullInfo();

}