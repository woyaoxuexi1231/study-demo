package com.hundsun.demo.springcloud.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.security.config
 * @className: SecurityConfig
 * @description: 配置如何验证用户信息
 * @author: h1123
 * @createDate: 2023/5/9 0:05
 */

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 通过 AuthenticationManagerBuilder 创建一个认证用户的信息
     * <p>
     * 用户名为 admin, 密码为 admin, USER 的角色
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    //     authenticationManagerBuilder
    //             .inMemoryAuthentication()
    //             .passwordEncoder(new BCryptPasswordEncoder())
    //             .withUser("admin")
    //             .password(new BCryptPasswordEncoder().encode("admin"))
    //             .roles("ADMIN", "USER");
    //     authenticationManagerBuilder
    //             .inMemoryAuthentication()
    //             .passwordEncoder(new BCryptPasswordEncoder())
    //             .withUser("test")
    //             .password(new BCryptPasswordEncoder().encode("test"))
    //             .roles("USER");
    // }

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobalMysql(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 配置 HttpSecurity
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/blogs/**").hasRole("USER")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login-error")
                .and()
                .exceptionHandling().accessDeniedPage("/401");
        http.logout().logoutSuccessUrl("/");
    }
}
