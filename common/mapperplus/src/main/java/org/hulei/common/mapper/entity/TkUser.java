package org.hulei.common.mapper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
