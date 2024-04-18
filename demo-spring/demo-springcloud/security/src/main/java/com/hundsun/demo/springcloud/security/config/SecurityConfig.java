package com.hundsun.demo.springcloud.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication() // 这是AuthenticationManagerBuilder对象的一个方法调用，它表示将在内存中进行身份验证配置。
    //             .passwordEncoder(new BCryptPasswordEncoder()) // 这一行指定了密码编码器为BCryptPasswordEncoder，用于对密码进行加密和验证。
    //             .withUser("forezp") // 这一行指定了一个用户名为"forezp"的用户。
    //             .password(new BCryptPasswordEncoder().encode("123456")) // 这一行指定了用户的密码，并使用BCryptPasswordEncoder对密码进行加密。
    //             .roles("USER"); // 这一行指定了用户的角色为"USER"，表示该用户具有"USER"角色权限。
    //     auth.inMemoryAuthentication()
    //             .passwordEncoder(new BCryptPasswordEncoder())
    //             .withUser("admin")
    //             .password(new BCryptPasswordEncoder().encode("123456"))
    //             .roles("ADMIN"); // 在仅有admin角色的权限的情况下,访问需要user角色的页面是访问不了的
    //     // .roles("ADMIN", "USER");
    // }

    // @Autowired
    // UserDetailsService userDetailsService;
    //


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }


    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("123456"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }


    // /**
    //  * 配置 HttpSecurity
    //  *
    //  * @param http the {@link HttpSecurity} to modify
    //  * @throws Exception
    //  */
    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //     http.authorizeRequests() // 这是HttpSecurity对象的一个方法调用，用于配置请求的授权规则。
    //             .antMatchers("/css/**", "/index").permitAll() //  这一行指定了对于"/css/**“和”/index"路径的请求，允许所有用户访问，不需要进行身份验证。
    //             .antMatchers("/user/**").hasRole("USER") //  这一行指定了对于"/user/**"路径的请求，要求用户具有"USER"角色才能访问。
    //             .antMatchers("/blogs/**").hasRole("USER") // 这一行指定了对于"/blogs/**"路径的请求，同样要求用户具有"USER"角色才能访问。
    //             .and() // 这是一个方法调用，表示对于前面的授权规则链进行连接。
    //             .formLogin().loginPage("/login").failureUrl("/login-error") // 这一行配置了基于表单的登录，指定了登录页面为"/login"，登录失败后跳转到"/login-error"页面。
    //             .and() // 同样是一个方法调用，表示对前面的配置进行连接。
    //             .exceptionHandling().accessDeniedPage("/401"); // 这一行配置了异常处理，指定了访问被拒绝时跳转到"/401"页面。
    //     http.logout().logoutSuccessUrl("/"); // 这一行配置了退出登录，指定了退出成功后跳转到"/"路径。
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/", "/login", "/bootstrap/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user", true)
                .permitAll()
                // .and()
                // .logout()
                // .logoutUrl("/logout") // 指定登出路径
                // .logoutSuccessUrl("/index") // 指定登出成功后跳转的页面
                // .invalidateHttpSession(true) // 使 HTTP Session 失效
                // .deleteCookies("JSESSIONID") // 删除指定的 Cookie
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/401");
        http.logout().logoutSuccessUrl("/"); // 这一行配置了退出登录，指定了退出成功后跳转到"/"路径。
    }


}
