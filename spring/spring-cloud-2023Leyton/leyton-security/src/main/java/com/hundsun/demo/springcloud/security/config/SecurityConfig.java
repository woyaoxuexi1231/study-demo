package com.hundsun.demo.springcloud.security.config;

import com.hundsun.demo.springcloud.security.authenticationprovider.DeviceAuthenticationDetails;
import com.hundsun.demo.springcloud.security.filter.CaptchaFilter;
import com.hundsun.demo.springcloud.security.handler.CustomAuthenticationFailureHandler;
import com.hundsun.demo.springcloud.security.mapper.MyPersistentTokenRepository;
import com.hundsun.demo.springcloud.security.userdetailservice.DbUserDetailServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    @Autowired
    MyPersistentTokenRepository myPersistentTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
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
                // })、

                /*
                登录成功后无论用户之前访问了哪个页面，都强制跳转到指定的页面

                如果不写，遵循如下行为：
                    1. 有访问受保护页面时，被拦截到 /login，登录成功后会自动跳回用户原来请求的页面（SavedRequest）
                    2. 没有访问受保护页面，直接访问 /login，登录成功后就会跳到 /（根路径）
                 */
                .defaultSuccessUrl("/home", true)
                .permitAll()

                .and()

                .logout()
                .permitAll()

                .and()

                /*
                增加自动登录功能，默认为简单散列加密
                这是一种极其简单的 remember me 功能
                对用户名+密码+过期时间+随机散列值(应用启动之后确定) 来生成 token，这意味着应用重启后，记住功能失效

                .tokenRepository(myPersistentTokenRepository) 持久化形式的自动登录
                这种方式采用 series 和 token 两个值，它们都是用MD5散列过的随机字符串。
                series仅在用户使用密码重新登录时更新，而token会在每一个新的session中都重新生成。
                 - 首先，解决了散列加密方案中一个令牌可以同时在多端登录的问题。每个会话都会引发token的更新，即每个token仅支持单实例登录。
                 - 其次，自动登录不会导致series变更，而每次自动登录都需要同时验证series和token两个值
                   ，当该令牌还未使用过自动登录就被盗取时，系统会在非法用户验证通过后刷新 token 值，此时在合法用户的浏览器中，该token值已经失效。
                   当合法用户使用自动登录时，由于该series对应的 token 不同，系统可以推断该令牌可能已被盗用，从而做一些处理。
                   例如，清理该用户的所有自动登录令牌，并通知该用户可能已被盗号等。
                 - 服务重启也不会影响现有的自动登录

                 TODO 这里关于单点登录的问题
                 */
                .rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(myPersistentTokenRepository)
        ;

        http.csrf().disable();

        // 验证码校验的过滤器 先于 账户验证过滤器执行
        http.addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
