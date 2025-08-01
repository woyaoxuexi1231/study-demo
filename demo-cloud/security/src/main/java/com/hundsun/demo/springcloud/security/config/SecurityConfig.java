package com.hundsun.demo.springcloud.security.config;

import com.hundsun.demo.springcloud.security.filter.CaptchaFilter;
import com.hundsun.demo.springcloud.security.handler.CustomAuthenticationFailureHandler;
import com.hundsun.demo.springcloud.security.mapper.MyPersistentTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hulei
 * @since 2025/7/25 13:35
 */

@RequiredArgsConstructor
@Configuration
/*
@EnableWebSecurity 是开启 spring security 的默认行为，导入了 WebSecurityConfiguration
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

TODO 源码比较复杂，还要再看
WebSecurityConfiguration 用于初始化 webSecurity 配置
  ⭐ springSecurityFilterChain 这个方法是 Spring Security 运行时注入的 核心过滤器链 Bean。
      - 如果用户没有自定义 WebSecurityConfigurer，就默认使用一个空实现（也就是WebSecurityConfigurerAdapter的匿名子类）。
      - webSecurity.build() 会构建出最终的 FilterChainProxy，这是 Spring Security 最终挂到 Servlet 容器中的过滤器。
        而 SecurityFilterChain 在当前类被注册成 bean，注入到 WebSecurityConfiguration 后，由它注入到 Servlet 容器中
 */
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> deviceAuthenticationDetailsSource;
    private final AuthenticationProvider authenticationProvider;
    private final MyPersistentTokenRepository myPersistentTokenRepository;
    private final UserDetailsService userDetailsService;
    private final SpringSessionBackedSessionRegistry registry;

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

                /*
                security 默认会注册一个 /logout 路由，访问该路径可以安全的注销登录状态
                包括清理 HttpSession失效、清空已配置的Remember-me验证，以及清空SecurityContextHolder，并在注销成功之后重定向到/login?logout页面。
                和登录接口一样，也是可以自定义一些相关的配置
                 */
                .logout()
                .permitAll()

                .and()

                /*
                使用 .rememberMe() + .userDetailsService
                增加自动登录功能，默认为简单散列加密，这是一种极其简单的 remember me 功能

                启动后，登陆成功可以在 cookie 中看到一个 remember-me 的新的 key
                对用户名+密码+过期时间+随机散列值(应用启动之后确定) 来生成 token，使用token用户的信息来生成，所以需要配置 userDetailsService

                整个流程是：
                  - 首先会注册一个 RememberMeAuthenticationFilter 的过滤器，在请求到达之后进行一系列的操作
                  - 登录请求进来后会通过 UsernamePasswordAuthenticationFilter 进行权限校验
                  - 校验成功后会在 UsernamePasswordAuthenticationFilter 进行登录成功后的操作，这里就包括生成 remember-me token
                  - 在没有使用 .tokenRepository 的情况下，token是在内存中的，每次新的会话时，cookie会带上 remember-me token ，每次校验这个 token 就可以知道登录状态有没有了

                问题：
                  1. token在内存中，这意味着应用重启后，记住功能失效
                  2. 分布式部署时（多台服务器），令牌无法同步，导致用户在不同服务器上登录状态不一致。
                 */
                // .rememberMe()
                // .rememberMeServices(rememberMeServices())
                // .userDetailsService(userDetailsService)
                /*
                .tokenRepository(myPersistentTokenRepository) 持久化形式的自动登录
                这里 PersistentTokenRepository 不一定要使用 jdbc，redis都是可以的

                使用 series 和 token
                    series：使用安全的随机数生成器（如 SecureRandom）生成一个唯一字符串（如 64 位十六进制数），确保全局唯一。
                    token：同样使用随机数生成器生成另一个唯一字符串（与 series 长度相同）。

                验证令牌有效性：
                    若 series 不存在：令牌无效，拒绝访问。
                    若 series 存在但 token 不匹配：说明令牌已被刷新或伪造，删除旧 series 记录并拒绝访问。(这里其实就可以推断已经被盗用了)
                    若 token 匹配：更新 last_used 为当前时间（实现滑动过期），并生成新的 token（可选，增强安全性）：
                        生成新的 token 并更新数据库中的 token 字段（旧 token 失效）。
                        客户端下次请求时携带新的 series:new_token，重复验证流程。

                 TODO 这里关于单点登录的问题
                 */
                // .tokenRepository(myPersistentTokenRepository)
        ;

        /*
        sessionManagement 是一个会话管理的配置器，其中有关于防御会话固定攻击的四种策略
          - none：不做任何变动，登录之后沿用旧的session。
          - newSession：登录之后创建一个新的session。
          - migrateSession：登录之后创建一个新的session，并将旧的session中的数据复制过来。 默认启动
          - changeSessionId：不创建新的会话，而是使用由Servlet容器提供的会话固定保护。

        TODO 会话攻击
        在 Spring Security 中，即便没有配置，也大可不必担心会话固定攻击。
        Spring Security 的 HTTP 防火墙会帮助我们拦截不合法的URL
        当我们试图访问带session的URL时，实际上会被重定向到类似如图6-1所示的错误页。(默认行为)

        开启 remember-me 功能时，其实这个自定义失效策略就不会再使用了，因为有记住我的功能，即使会话失效了，也会续约
         */
        http.sessionManagement()
                // .sessionFixation().none()
                // 配置 session 失效策略，这里是一个自定义的失效策略，返回一串提示
                // .invalidSessionStrategy(new MyInvalidSessionStrategy())
                // 最大会话数量，新登录的会话会把之前的会话给剔除
                .maximumSessions(1)
                /*
                阻止新会话建立，而不是踢掉旧的会话
                ❗问题1：当前已登录的会话在注销登陆后，将无法再登录。
                ⚠️原因：
                    当用户主动注销（logout()）后，SessionRegistry 默认不会自动移除会话信息，除非你手动配置它。
                    因此，即便用户退出登录，看似没有活跃会话了，但 SessionRegistry 仍认为这个用户在“占用会话”，于是后续任何新的用户都无法登录。
                ❓为什么 Spring Security 不默认处理这个问题？
                    1. 注销不一定等于 Session 销毁，在某些场景中用户可能只是“退出身份验证”，但希望保留 Session（比如购物车等功能）。
                    2. 可扩展性考虑，Spring 认为这是开发者应该根据业务场景做出的选择。
                    3. 默认行为更通用、安全，不贸然清除 session 信息。
                💡解决方案：
                    方案一：配置 HttpSessionEventPublisher 监听注销事件
                    方案二：logout 中添加监听器或手动清理 SessionRegistry 中的会话信息（如果你使用了自定义的 LogoutHandler）。


                ❗问题2：在使用持久化 UserDetailsService 的时候要注意 User 必须要重写 hashcode 和 equals
                ⚠️原因：
                    spring 使用 SessionRegistryImpl 保存用户的session信息
                    在不重写 hashcode 和 equals 的情况下，只要 user 用户对象不同，即使是同一个用户也会跳过登录限制
                 */
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(registry)
        ;

        // 验证码校验的过滤器 先于 账户验证过滤器执行
        http.addFilterBefore(new CaptchaFilter(), UsernamePasswordAuthenticationFilter.class);

        /*
        跨域是一种浏览器同源安全策略，即浏览器单方面限制脚本的跨域访问。
        实际上，不仅不同站点间的访问存在跨域问题，同站点间的访问可能也会遇到跨域问题，只要请求的URL与所在页面的URL首部不同即产生跨域，例如：
          ◎ 在http://a.baidu.com下访问https://a.baidu.com资源会形成协议跨域。
          ◎ 在a.baidu.com下访问b.baidu.com资源会形成主机跨域。
          ◎ 在a.baidu.com:80下访问a.baidu.com:8080资源会形成端口跨域。
        从协议部分开始到端口部分结束，只要与请求URL不同即被认为跨域，域名与域名对应的IP也不能幸免。

        CORS（Cross-Origin Resource Sharing）的规范中有一组新增的HTTP首部字段，允许服务器声明其提供的资源允许哪些站点跨域使用。
        浏览器首先会发起一个请求方法为OPTIONS 的预检请求，用于确认服务器是否允许跨域，只有在得到许可后才会发出实际请求。

        spring security 对于 cors 提供了非常好的支持
          1. 在此配置中添加 http.cors(); 开启cors支持
          2. 编写一个 cors 配置源
         */
        http.cors();

        /*
        CSRF（Cross-Site Request Forgery，跨站请求伪造）是一种攻击方式：
          1. 攻击者诱骗用户（已登录状态）访问恶意网站
          2. 该网站自动向目标网站（如银行网站）发送伪造请求
          3. 利用用户的登录凭证完成非法操作（如转账）

        常见禁用场景：
          1. 纯 API 服务（无浏览器会话，使用 JWT 等无状态认证）
          2. 测试环境简化配置
          3. 与某些传统前端框架集成困难时
         */
        http.csrf().disable();

        return http.build();
    }
}
