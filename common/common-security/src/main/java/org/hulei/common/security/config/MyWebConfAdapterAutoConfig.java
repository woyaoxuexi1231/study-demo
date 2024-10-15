package org.hulei.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * 继承 WebSecurityConfigurerAdapter 来实现一个 Spring Security 的配置
 * 1. 安全配置的功能：
 * configure(AuthenticationManagerBuilder auth)：用于配置用户细节和认证类型，例如内存身份验证、JDBC身份验证或者LDAP身份验证。
 * configure(HttpSecurity http)：用于基于HTTP请求的授权配置，包括哪些路径需要验证，哪些路径对特定角色开放，定义登录和注销的处理方式等。
 * 2. 自定义登录页面,异常页面
 * .formLogin()
 * <p>
 * 但目前此类已经不再推荐使用,推荐使用 SecurityFilterChain 来进行配置
 *
 * @author h1123
 * @since 2023/5/9 0:05
 */

@ConditionalOnProperty(name = "common.security.strategy", havingValue = "configureradapter")
@EnableWebSecurity
@Configuration
public class MyWebConfAdapterAutoConfig extends WebSecurityConfigurerAdapter {

    /**
     * 通过这个方法可以配置各种资源的权限访问级别, 同时可以配置在各种异常情况下的处理方式
     * 如果不重写这个方法, Spring Security 框架默认实现了登录功能, 以及对所有资源的访问都需要验证权限的默认配置
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception e
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // 这是HttpSecurity对象的一个方法调用，用于配置请求的授权规则。
                .antMatchers("/css/**", "/fonts/**").permitAll()  // 基于路径的匹配规则, 所有以 /css/开头的, /fonts/开头的url请求全部不需要身份验证,无论是否登录都可以访问
                .antMatchers("/", "/index", "/error", "/401").permitAll() // 配置主页无限制访问
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // 配置user路径仅user角色可以访问
                .anyRequest() // 匹配应用程序中的所有 HTTP 请求，无论其路径或类型（GET、POST 等）。
                .authenticated() // 表示所有匹配的请求都必须经过身份验证，只有已登录并且成功认证的用户才可以访问这些请求。
                .and()

                // 指定登录页面为 /login, 默认的登录成功后的页面是 /user/index, ture代表登录成功后总是会重定向到指定的 /user/index 页面，无论用户之前访问了什么 URL, 默认的登录失败的页面为 /error
                .formLogin().loginPage("/login").defaultSuccessUrl("/user/index", true).failureForwardUrl("/error")
                .and()
                // 主要用于处理用户访问受保护资源时出现的异常情况, 例如未授权访问或没有登录时的行为
                .exceptionHandling()
                // .accessDeniedPage("/401") // 这个方法是用来处理已经通过身份验证但是没有足够权限访问特定资源的用户。
                // accessDeniedHandler相比accessDeniedPage允许配置更加复杂的逻辑
                // 开启了exceptionHandling()之后,继续进行流式的配置,这里用于处理已登录但没有访问权限的用户
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String username = authentication.getName(); // 获取登录用户的账户名
                    request.setAttribute("username", username);
                    request.setAttribute("isLogin", true);
                    // request.getRequestDispatcher("/401").forward(request, response);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("权限不足以访问此资源!");
                })
                // 处理未认证用户访问受保护资源时的行为, 主要用于处理未认证的问题, 也就是未登录
                .authenticationEntryPoint((request, response, authException) -> {
                    request.setAttribute("isLogin", false);
                    // request.getRequestDispatcher("/401").forward(request, response); // 指定了访问被拒绝时跳转到"/401"页面。
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("需要登录后才能访问此资源!");
                })
        ;
        http.logout().logoutSuccessUrl("/"); // 这一行配置了退出登录，指定了退出成功后跳转到"/"路径。
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 这是AuthenticationManagerBuilder对象的一个方法调用，它表示将在内存中进行身份验证配置。
                .passwordEncoder(new BCryptPasswordEncoder()) // 这一行指定了密码编码器为BCryptPasswordEncoder，用于对密码进行加密和验证。
                .withUser("user-adapter") // 这一行指定了一个用户名为"forezp"的用户。
                .password(new BCryptPasswordEncoder().encode("123456")) // 这一行指定了用户的密码，并使用BCryptPasswordEncoder对密码进行加密。
                .roles("USER"); // 这一行指定了用户的角色为"USER"，表示该用户具有"USER"角色权限。
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin-adapter")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN"); // 在仅有admin角色的权限的情况下,访问需要user角色的页面是访问不了的
    }
}
