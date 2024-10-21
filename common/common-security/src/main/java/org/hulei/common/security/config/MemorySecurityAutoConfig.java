package org.hulei.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

/**
 * @author hulei
 * @since 2024/10/14 15:56
 */

@ConditionalOnProperty(name = "common.security.strategy", havingValue = "memory")
@Slf4j
@Configuration
public class MemorySecurityAutoConfig {

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

    // /**
    //  * 这是一个完整的内存验证配置
    //  * 基于内存的方式,这里提供了两种方式进行配置
    //  * 1. inMemoryAuthentication方法
    //  * 2. 直接设置一个基于内存的userDetailsService
    //  *
    //  * @param auth AuthenticationManagerBuilder
    //  * @throws Exception ..
    //  */
    // @Autowired
    // public void configureGlobal(
    //         AuthenticationManagerBuilder auth,
    //         @Qualifier("myInMemoryUserDetailsManager") InMemoryUserDetailsManager userDetailsManager
    // ) throws Exception {
    //
    //     // auth.inMemoryAuthentication() // 这是AuthenticationManagerBuilder对象的一个方法调用，它表示将在内存中进行身份验证配置。
    //     //         .passwordEncoder(new BCryptPasswordEncoder()) // 这一行指定了密码编码器为BCryptPasswordEncoder，用于对密码进行加密和验证。
    //     //         .withUser("user-memory") // 这一行指定了一个用户名为"forezp"的用户。
    //     //         .password(new BCryptPasswordEncoder().encode("123456")) // 这一行指定了用户的密码，并使用BCryptPasswordEncoder对密码进行加密。
    //     //         .roles("USER"); // 这一行指定了用户的角色为"USER"，表示该用户具有"USER"角色权限。
    //     // auth.inMemoryAuthentication()
    //     //         .passwordEncoder(new BCryptPasswordEncoder())
    //     //         .withUser("admin-memory")
    //     //         .password(new BCryptPasswordEncoder().encode("123456"))
    //     //         .roles("ADMIN"); // 在仅有admin角色的权限的情况下,访问需要user角色的页面是访问不了的
    //
    //     auth.userDetailsService(userDetailsManager);
    // }
}
