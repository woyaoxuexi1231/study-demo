package org.hulei.springboot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hulei.eneity.mybatisplus.domain.Items;

import java.util.List;

public interface ItemsMapper extends BaseMapper<Items> {
    /**
     * 主键自增插入
     *
     * @param itemDO items
     */
    void insertItemsAutoKey(@Param("items") Items itemDO);

    /**
     * 自定义主键插入
     *
     * @param itemDO items
     */
    void insertItemsCustomKey(@Param("items") Items itemDO);

    void insertItemsAutoKeyList(List<Items> itemDOList);

    void insertItemsCustomKeyList(List<Items> itemDOList);
}