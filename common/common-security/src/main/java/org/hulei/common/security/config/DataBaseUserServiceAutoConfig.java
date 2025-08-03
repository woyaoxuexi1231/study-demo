package org.hulei.common.security.config;

import org.hulei.common.security.service.impl.MyUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * UserDetailsService用于配置用户的详细信息
 * WebSecurityConfigurerAdapter用于整体安全配置，包括请求授权、HTTP认证、Web安全功能设置等。
 * UserDetailsService主要用于用户数据的获取，被Spring Security用于认证过程，提供用户信息和认证详细数据。
 *
 * @author hulei
 * @since 2024/10/14 16:10
 */

@ConditionalOnProperty(name = "common.security.userservice.strategy", havingValue = "database", matchIfMissing = true)
@Configuration
public class DataBaseUserServiceAutoConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 配置密码编码器（PasswordEncoder）
        // 密码编码器没有进行实际的加密操作，它只是将密码以明文形式存储
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(12);
    }

    @Bean(name = "myUserService")
    public MyUserService myUserService() {
        return new MyUserService();
    }
}
