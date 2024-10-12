package org.hulei.common.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.common.mapper.entity.ProductFullInfo;
import org.hulei.common.mapper.entity.pojo.ProductsDO;

import java.util.List;

public interface ProductsMapper extends BaseMapper<ProductsDO> {

    List<ProductFullInfo> selectProductFullInfo();
}