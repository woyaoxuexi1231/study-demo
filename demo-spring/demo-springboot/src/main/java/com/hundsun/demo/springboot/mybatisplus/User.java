package com.hundsun.demo.springboot.mybatisplus;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * 用户信息实体对象
 *
 * @author chendd
 * @date 2022/6/30 21:30
 */

@Data
@TableName(value = "user")
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}