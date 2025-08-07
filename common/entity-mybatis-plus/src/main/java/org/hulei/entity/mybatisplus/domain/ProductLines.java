package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "test_product_lines")
public class ProductLines {

    @TableId(value = "product_line", type = IdType.AUTO)
    private String productLine;

    @TableField(value = "text_description")
    private String textDescription;

    @TableField(value = "html_description")
    private String htmlDescription;

    @TableField(value = "image")
    private byte[] image;
}