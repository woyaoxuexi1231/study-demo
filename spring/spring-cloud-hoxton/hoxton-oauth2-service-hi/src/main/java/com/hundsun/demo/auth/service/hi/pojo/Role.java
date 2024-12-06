package com.hundsun.demo.auth.service.hi.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.pojo
 * @className: Role
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:48
 */

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
