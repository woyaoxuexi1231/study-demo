package com.hundsun.demo.springcloud.security.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author hulei
 * @since 2025/7/24 15:33
 */




/*
允许在方法级别进行安全验证。prePostEnabled 参数设置为 true 启用了 Pre 和 Post 注解。
这意味着你可以在方法上使用 @PreAuthorize 和 @PostAuthorize 注解进行访问控制的设置。

例如，你可以在方法上使用 @PreAuthorize 注解来指定需要的权限或角色。
 */
// @EnableGlobalMethodSecurity(prePostEnabled = true)
/*
Spring Security 的核心注解，用于启用和配置 Web 安全功能

1. 启用 Spring Security 的 Web 安全支持
    - 它会自动加载 Spring Security 的默认配置
    - 激活 Web 安全相关的组件和过滤器链
2. 标记配置类
    - 标识这个类是一个 Spring Security 的配置类
    - 通常与 @Configuration 注解一起使用
3. 替代旧版 XML 配置
    - 在基于 Java 的配置中替代了 <http> 等 XML 配置元素

当使用 @EnableWebSecurity 时，它会：
- 导入 WebSecurityConfiguration 类
- 注册 SpringSecurityFilterChain 过滤器
- 设置默认的安全过滤器链

在 springboot2.3.12 这个版本中，其实已经有自动注入的适配器
配置类 SpringBootWebSecurityConfiguration 会自动一个 DefaultConfigurerAdapter
所以即使不自定义配置，在启动后也能看到 spring security 提供的默认表单登录
 */
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        WebSecurityConfigurerAdapter 已经默认声明了一些安全特性：
        1. 验证所有请求
        2. 允许用户使用表单登录进行身份验证（Spring Security 提供了一个简单的表单登录页面）。
        3. 允许用户使用 HTTP 基本认证。
         */
        super.configure(http);

        /*
        虽然自动生成的表单登录页方便、快捷。
        但是大多数时候，大多数应用都希望提供更加个性化的登录表单。
        所以仅仅使用 WebSecurityConfigurerAdapter 提供的默认配置不足以满足要求。
        重写 configure 方法就行。

        HttpSecurity 实际上就是在配置 Spring Security 的过滤链。
        formLogin、exceptionHandling、csrf 每个配置都对应一个过滤器
         */
        // http.authorizeRequests()
        //
        //         // 配置静态资源的无限制访问
        //         .antMatchers("/css/**", "/fonts/**", "/js/**").permitAll()  // 仅允许样式静态资源的无限制访问
        //         .antMatchers("/captcha.jpg").permitAll()
        //         // 按照不同接口，配置不同角色的限制访问
        //         .antMatchers("/user/**").hasRole("USER") // 配置 user 路径仅 user 角色可以访问
        //         .antMatchers("/admin/**").hasRole("ADMIN") // 配置 admin 路径仅 admin 角色可以访问
        //         .antMatchers("/app/**").permitAll() // 配置 app 路径所有用户都可以访问
        //
        //         // 设置请求的授权规则
        //         .anyRequest() // 匹配所有请求
        //         .authenticated() // 要求所有请求都必须经过认证（用户必须登录）
        //
        //         // and 之后将返回 HttpSecurity 上下文，可以继续链式配置其他选项
        //         .and()
        //
        //         // 配置表单登录相关设置
        //         .formLogin() // 启用表单登录功能。TODO 如果不开启这个配置，还能登陆吗？
        //         // 指定自定义登录页面（而不是使用默认的/login）。可以配置页面，也可以配置连接
        //         // 使用 thymeleaf 这类模板时，再填写登录页面就不再合适，因为模板文件一般存在 templates 文件夹下，而不在 static 下面，而直接填写页面会在 static 下面去找，这会导致找不到页面报错 404
        //         .loginPage("/login-form")
        //         // .loginProcessingUrl("/login") // 有时候登录 url 并不是默认的 login，这个时候就可以通过这个配置来自定义 url
        //         // .failureForwardUrl("/error")
        //         .permitAll() // 允许所有用户（包括未认证用户）访问登录页面，配置了这个之后就不再需要配置 antMatchers 来进行限制放开
        //         // 配置登录成功后的处理逻辑
        //         // .successHandler((request, response, authentication) -> response.sendRedirect("/user/index"))
        //         // .failureHandler((request, response, exception) -> {
        //         //     response.setContentType("application/json;charset=utf-8");
        //         //     response.setStatus(401);
        //         //     PrintWriter writer = response.getWriter();
        //         //     writer.write("401! " + exception.getMessage());
        //         // })
        //         // .successForwardUrl("/index")
        //         // .defaultSuccessUrl("/index")
        //
        //         // .and()
        //         //
        //         // // 这是 Spring Security 提供的异常处理入口，用于自定义各种安全异常的处理方式。
        //         // .exceptionHandling()
        //         // // accessDeniedHandler - 处理已认证但无权限的访问
        //         // .accessDeniedHandler((request, response, accessDeniedException) -> {
        //         //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //         //     String username = authentication.getName(); // 获取登录用户的账户名
        //         //     request.setAttribute("username", username);
        //         //     request.setAttribute("isLogin", true);
        //         //     request.getRequestDispatcher("/401").forward(request, response);
        //         // })
        //         // // authenticationEntryPoint - 处理未认证的访问
        //         // .authenticationEntryPoint((request, response, authException) -> {
        //         //     request.setAttribute("isLogin", false);
        //         //     request.getRequestDispatcher("/401").forward(request, response);
        //         // }) // 这个方法是用来处理未经身份验证就试图访问受保护资源的用户
        //         // // 这是一个简化配置，只能指定错误页面的 URL，使用重定向而非转发，逻辑简单，无参数传递
        //         // // .accessDeniedPage("/401")
        //
        //         .and()
        //
        //         // 配置CSRF（跨站请求伪造）防护
        //         .csrf() // csrf() 跨站请求伪造防护功能，（默认是启用的）
        //         .disable() // 关闭了Spring Security的CSRF保护功能，通常只有在API服务或特殊情况下才会禁用
        //
        //         .sessionManagement()
        //         .maximumSessions(1);

        // http.addFilterBefore(
        //         new VerificationCodeFilter(),
        //         UsernamePasswordAuthenticationFilter.class
        // );
        // http.logout().logoutSuccessUrl("/"); // 这一行配置了退出登录，指定了退出成功后跳转到"/"路径。

    }

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     /*
    //     这个方法同样支持我们配置账户信息，最终实现的效果和使用 UserDetailsService 如出一辙。
    //      */
    //     // auth.inMemoryAuthentication() // 这是AuthenticationManagerBuilder对象的一个方法调用，它表示将在内存中进行身份验证配置。
    //     //         .passwordEncoder(new BCryptPasswordEncoder()) // 这一行指定了密码编码器为BCryptPasswordEncoder，用于对密码进行加密和验证。
    //     //         .withUser("user") // 这一行指定了一个用户名为"forezp"的用户。
    //     //         .password(new BCryptPasswordEncoder().encode("123456")) // 这一行指定了用户的密码，并使用BCryptPasswordEncoder对密码进行加密。
    //     //         .roles("USER"); // 这一行指定了用户的角色为"USER"，表示该用户具有"USER"角色权限。
    //     // auth.inMemoryAuthentication()
    //     //         .passwordEncoder(new BCryptPasswordEncoder())
    //     //         .withUser("admin")
    //     //         .password(new BCryptPasswordEncoder().encode("123456"))
    //     //         .roles("ADMIN","USER"); // 在仅有admin角色的权限的情况下,访问需要user角色的页面是访问不了的
    //
    //     // 仅做演示，就不在这里配置。
    //     super.configure(auth);
    // }
}
