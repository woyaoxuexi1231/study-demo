package com.hundsun.demo.commom.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hulei42031
 * @since 2024-04-03 17:11
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Data
public class TkUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Mysql")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    // 省略构造函数和 getter/setter 方法

    public TkUser(String name) {
        this.name = name;
    }
}
