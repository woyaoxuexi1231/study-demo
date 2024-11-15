package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hulei.eneity.mybatisplus.domain.OrderDetails;

import java.util.List;

public interface OrderDetailsMapper extends BaseMapper<OrderDetails> {
    List<OrderDetails> selectByOrderId(@Param("ordernumber") Long orderId);
}