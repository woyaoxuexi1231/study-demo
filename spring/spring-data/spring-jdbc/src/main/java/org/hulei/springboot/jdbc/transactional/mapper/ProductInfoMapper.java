package org.hulei.springboot.jdbc.transactional.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.eneity.mybatisplus.domain.ProductInfo;

import java.util.List;

/**
 * @author hulei
 * @since 2024/11/17 22:10
 */

public interface ProductInfoMapper extends BaseMapper<ProductInfo> {
    /**
     * 批量插入数据
     *
     * @param list list
     */
    void batchInsert(List<ProductInfo> list);
}
