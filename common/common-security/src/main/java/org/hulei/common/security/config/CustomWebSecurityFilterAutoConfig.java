package org.hulei.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

/**
 * SecurityFilterChain是Spring Security 5.4引入的一种用于配置安全性过滤器链的机制，它通常结合Spring Security的HttpSecurity来使用，用于替代过去使用的WebSecurityConfigurerAdapter
 * 1. SecurityFilterChain 用于定义应用程序的顺序过滤器链，包括一些关键的安全功能，如身份验证、授权、CSRF保护等。
 * 2. 通过定义SecurityFilterChain bean来指定哪些安全策略（如URL保护、授权规则）应该应用到HTTP请求中。
 *
 * @author hulei
 * @since 2024/10/15 13:40
 */

@ConditionalOnProperty(name = "common.security.default.websecurity", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableWebSecurity
public class CustomWebSecurityFilterAutoConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll();
        return http.build();
    }
}