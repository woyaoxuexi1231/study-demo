package com.hundsun.demo.commom.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hulei
 * @since 2024/8/28 23:27
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "items")
public class ItemDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "item_no")
    private String itemNo;
}
