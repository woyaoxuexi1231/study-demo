package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.entity.mybatisplus.domain.Products;
import org.hulei.springboot.mybatisplus.model.ProductFullInfo;

import java.util.List;

public interface ProductsMapper extends BaseMapper<Products> {
    List<ProductFullInfo> selectProductFullInfo();
}