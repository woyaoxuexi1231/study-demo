package org.hulei.entity.mybatisplus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "big_data_users")
public class BigDataUsers {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "email")
    private String email;

    @TableField(value = "created_at")
    private Date createdAt;
}