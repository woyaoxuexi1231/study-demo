package org.hulei.commom.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.commom.core.model.ProductFullInfo;
import org.hulei.commom.core.model.pojo.ProductsDO;

import java.util.List;

public interface ProductsMapper extends BaseMapper<ProductsDO> {

    List<ProductFullInfo> selectProductFullInfo();
}