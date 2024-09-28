package org.hulei.commom.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.hulei.commom.core.model.pojo.OrderdetailsDO;

import java.util.List;

public interface OrderdetailsMapper {

    List<OrderdetailsDO> selectByOrderId(@Param("ordernumber") Long orderId);

}