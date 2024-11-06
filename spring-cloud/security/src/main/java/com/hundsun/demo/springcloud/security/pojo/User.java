package com.hundsun.demo.springcloud.security.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.pojo
 * @className: User
 * @description:
 * @author: h1123
 * @createDate: 2023/5/9 20:42
 */

@Entity
@Data
public class User implements UserDetails, Serializable {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String password;

    // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    //         inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 指示用户帐户是否已过期。过期的帐户无法进行身份验证。
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指示用户是锁定还是解锁。无法对锁定的用户进行身份验证。
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示用户的凭据（密码）是否已过期。过期的凭据会阻止身份验证。
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 指示用户是启用还是禁用。无法对已禁用的用户进行身份验证。
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
