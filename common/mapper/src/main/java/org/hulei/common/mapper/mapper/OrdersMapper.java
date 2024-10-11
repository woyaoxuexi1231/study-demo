package org.hulei.common.mapper.mapper;

import org.hulei.common.mapper.entity.OrderFullInfo;

import java.util.List;

public interface OrdersMapper {

    List<OrderFullInfo> selectOrderFullInfo();

}