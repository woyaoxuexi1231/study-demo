package org.hulei.common.mapper.mapper;

import org.apache.ibatis.annotations.Param;
import org.hulei.common.mapper.entity.pojo.OrderdetailsDO;

import java.util.List;

public interface OrderdetailsMapper {

    List<OrderdetailsDO> selectByOrderId(@Param("ordernumber") Long orderId);

}