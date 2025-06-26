package org.hulei.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.hulei.entity.jpa.pojo.ProductInfo;

import java.util.List;

/**
 * @author hulei
 * @since 2025/6/26 16:24
 */

public interface ProductInfoMapper {
    int insert(ProductInfo record);
    void batchInsert(@Param(value = "records") List<ProductInfo> records);
    ProductInfo selectById(Integer id);
}
