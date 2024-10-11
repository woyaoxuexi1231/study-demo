package org.hulei.keeping.server.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.common.mapper.entity.pojo.ProductInfoDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {
    /**
     * 批量插入数据
     *
     * @param list list
     */
    void batchInsert(List<ProductInfoDO> list);
}
