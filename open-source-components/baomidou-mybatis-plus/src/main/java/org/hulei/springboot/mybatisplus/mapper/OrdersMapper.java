package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.entity.mybatisplus.domain.Orders;

import java.util.List;

public interface OrdersMapper extends BaseMapper<Orders> {

    List<Orders> selectOrderFullInfo();
}