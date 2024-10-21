package org.hulei.common.security.config;

import lombok.extern.slf4j.Slf4j;
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
 * 这个注解 EnableWebSecurity 用于启动 Spring Security的 Web 安全功能的注解
 * <p>
 * 1. 启用了Spring Security的Web安全功能，使得应用程序可以拦截HTTP请求并应用安全策略。
 * <p>
 * 2. 使用该注解后，你可以通过定义一个配置类来自定义Web安全配置。通常，这包括配置HTTP请求的授权规则、登录、注销、会话管理、CSRF保护等。
 * 自 Spring Security 5.4开始，配置Web安全性时推荐使用SecurityFilterChain来替代WebSecurityConfigurerAdapter，结合@EnableWebSecurity一起使用。
 * <p>
 * 3. 如果没有定义自己的安全配置，该注解会应用Spring Security的默认配置，这通常包括以下几项：
 * - 对所有请求进行认证。
 * - 提供一个默认的登录页面。
 * - 常见的安全保护，如防御CSRF攻击等。
 *
 * @author h1123
 * @since 2023/5/9 0:05
 */

@Slf4j
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
        log.info("配置权限规则");
        super.configure(http);
        http
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
                    response.setHeader("content-type", "text/html;charset=utf-8");
                    response.getWriter().write("权限不足以访问此资源!");
                })
                // 处理未认证用户访问受保护资源时的行为, 主要用于处理未认证的问题, 也就是未登录
                .authenticationEntryPoint((request, response, authException) -> {
                    request.setAttribute("isLogin", false);
                    // request.getRequestDispatcher("/401").forward(request, response); // 指定了访问被拒绝时跳转到"/401"页面。
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader("content-type", "text/html;charset=utf-8");
                    response.getWriter().write("需要登录后才能访问此资源!");
                })
        ;
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
