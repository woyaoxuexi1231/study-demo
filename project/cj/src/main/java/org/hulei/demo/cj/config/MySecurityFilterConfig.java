package org.hulei.demo.cj.config;

import org.hulei.common.security.config.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SecurityFilterChain是Spring Security 5.4引入的一种用于配置安全性过滤器链的机制，它通常结合Spring Security的HttpSecurity来使用，用于替代过去使用的WebSecurityConfigurerAdapter
 * 1. SecurityFilterChain 用于定义应用程序的顺序过滤器链，包括一些关键的安全功能，如身份验证、授权、CSRF保护等。
 * 2. 通过定义SecurityFilterChain bean来指定哪些安全策略（如URL保护、授权规则）应该应用到HTTP请求中。
 *
 * @author hulei
 * @since 2024/10/15 13:40
 */

@Configuration
@EnableWebSecurity
public class MySecurityFilterConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .regexMatchers(".*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK))
                .failureHandler(new CustomAuthenticationFailureHandler())
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                );
        ;

        http.cors();
        http.csrf().disable();

        return http.build();
    }
}