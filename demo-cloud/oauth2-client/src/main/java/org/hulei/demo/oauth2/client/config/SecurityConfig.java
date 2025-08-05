package org.hulei.demo.oauth2.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.ws.rs.HttpMethod;

/**
 * @author hulei
 * @since 2025/8/3 14:11
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http.authorizeHttpRequests(auth -> {
                    // auth.regexMatchers("/").permitAll();
                    auth
                            .regexMatchers("/error").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        /*
        UserDetailsService 是 Spring Security 框架中的一个核心接口
        负责加载用户特定的数据，是连接你的用户存储（数据库、LDAP、内存等）和 Spring Security 的桥梁。

        InMemoryUserDetailsManager是UserDetailsService接口中的一个实现类
        它将用户数据源寄存在内存里，在一些不需要引入数据库这种重数据源的系统中很有帮助。

        这里仅需要这样声明即可，spring security 会自动找到这个 bean，并做一些它应该做的工作
         */

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1"))
                .roles("USER")
                .accountLocked(true)
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
