package com.hundsun.demo.springboot.utils.mybatisplus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息实体对象
 *
 * @author chendd
 * @date 2022/6/30 21:30
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName(value = "user")
public class User {
    /**
     * primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名字
     */
    @TableField(value = "name")
    private String name;
    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
}