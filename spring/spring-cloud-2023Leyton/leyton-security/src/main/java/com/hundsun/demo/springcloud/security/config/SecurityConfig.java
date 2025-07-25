package com.hundsun.demo.springcloud.security.config;

import com.hundsun.demo.springcloud.security.authenticationprovider.DeviceAuthenticationDetails;
import com.hundsun.demo.springcloud.security.filter.CaptchaFilter;
import com.hundsun.demo.springcloud.security.handler.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hulei
 * @since 2025/7/25 13:35
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> deviceAuthenticationDetailsSource;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**").permitAll()  // 仅允许样式静态资源的无限制访问
                // 按照不同接口，配置不同角色的限制访问
                .antMatchers("/user/**").hasRole("USER") // 配置 user 路径仅 user 角色可以访问
                .antMatchers("/admin/**").hasRole("ADMIN") // 配置 admin 路径仅 admin 角色可以访问
                .antMatchers("/app/**").permitAll() // 配置 app 路径所有用户都可以访问
                .antMatchers("/captcha.jpg").permitAll()
                .antMatchers("/doLogin").permitAll()
                .anyRequest().authenticated()

                .and()

                .formLogin()
                .authenticationDetailsSource(deviceAuthenticationDetailsSource)
                // .authenticationDetailsSource(DeviceAuthenticationDetails::new)

                .loginPage("/login-form")
                .loginProcessingUrl("/doLogin") // 这个URL可以随便定义，并且不需要自己实现，这里仅仅只是改变登录地址而已，spring会自动变化
                // failureHandler 不允许配置多个，越后配置的才会生效
                .failureHandler(new CustomAuthenticationFailureHandler())
                // .failureHandler((request, response, exception) -> {
                //     System.out.println("Login failed: " + exception.getMessage());
                // })
                // .successHandler((request, response, authentication) -> {
                //     System.out.println("Login successful!");
                //     System.out.println("Redirecting to: " + request.getContextPath() + "/");
                //     response.sendRedirect(request.getContextPath() + "/");
                // })
                .defaultSuccessUrl("/home", true)

                /*
                登录成功后无论用户之前访问了哪个页面，都强制跳转到指定的页面

                如果不写，遵循如下行为：
                    1. 有访问受保护页面时，被拦截到 /login，登录成功后会自动跳回用户原来请求的页面（SavedRequest）
                    2. 没有访问受保护页面，直接访问 /login，登录成功后就会跳到 /（根路径）
                 */
                // .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http.csrf().disable();

        // 验证码校验的过滤器 先于 账户验证过滤器执行
        http.addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
