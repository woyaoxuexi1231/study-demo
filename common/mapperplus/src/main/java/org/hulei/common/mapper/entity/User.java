package org.hulei.common.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Builder
@NoArgsConstructor
@Entity
@Table(name = "users")
@TableName(value = "users")
@Data
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Mysql")
    @Column(name = "id")
    private Long id;

    @TableField(value = "name")
    @Column(name = "name")
    private String name;
    // 省略构造函数和 getter/setter 方法

    public User(String name) {
        this.name = name;
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
