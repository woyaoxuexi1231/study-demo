package com.hundsun.demo.auth.service.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

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
