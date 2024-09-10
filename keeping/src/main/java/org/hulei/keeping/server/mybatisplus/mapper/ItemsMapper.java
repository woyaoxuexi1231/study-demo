package org.hulei.keeping.server.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hulei.keeping.server.common.model.req.ItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hulei
 * @since 2024/8/28 23:31
 */

@Repository
public interface ItemsMapper extends BaseMapper<ItemDO> {
    /**
     * 主键自增插入
     *
     * @param itemDO items
     */
    void insertItemsAutoKey(@Param("items") ItemDO itemDO);

    /**
     * 自定义主键插入
     *
     * @param itemDO items
     */
    void insertItemsCustomKey(@Param("items") ItemDO itemDO);

    void insertItemsAutoKeyList(List<ItemDO> itemDOList);

    void insertItemsCustomKeyList(List<ItemDO> itemDOList);
}
