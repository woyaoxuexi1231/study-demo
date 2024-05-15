package com.hundsun.demo.springboot.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hundsun.demo.commom.core.model.ProductInfoDO;
import com.hundsun.demo.springboot.db.dynamicdb.annotation.TargetDataSource;
import com.hundsun.demo.springboot.db.dynamicdb.core.DynamicDataSourceType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInfoMapper extends BaseMapper<ProductInfoDO> {
    /**
     * 批量插入数据
     *
     * @param list list
     */
    @TargetDataSource(dataSourceType = DynamicDataSourceType.SECOND)
    void batchInsert(List<ProductInfoDO> list);
}
