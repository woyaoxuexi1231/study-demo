package org.hulei.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author hulei
 * @since 2024/10/14 15:56
 */

@ConditionalOnProperty(name = "common.security.userservice.strategy", havingValue = "memory")
@Slf4j
@Configuration
public class MemoryUserServiceAutoConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 配置密码编码器（PasswordEncoder）
        // 密码编码器没有进行实际的加密操作，它只是将密码以明文形式存储
        return NoOpPasswordEncoder.getInstance();
    }

    @Primary
    @Bean(name = "myInMemoryUserDetailsManager")
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);

    }
}
