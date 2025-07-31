package com.hundsun.demo.springcloud.security.authenticationprovider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/7/25 17:21
 */

/*
Spring Security提供了多种常见的认证技术，包括但不限于以下几种：
◎ HTTP层面的认证技术，包括HTTP基本认证和HTTP摘要认证两种。
◎ 基于LDAP的认证技术（Lightweight Directory Access Protocol，轻量目录访问协议）。
◎ 聚焦于证明用户身份的OpenID认证技术。
◎ 聚焦于授权的OAuth认证技术。
◎ 系统内维护的用户名和密码认证技术。
其中，使用最为广泛的是由系统维护的用户名和密码认证技术，通常会涉及数据库访问。

为了更好地按需定制，Spring Security 并没有直接糅合整个认证过程，而是提供了一个抽象的 AuthenticationProvider。
AbstractUserDetailsAuthenticationProvider 中实现了基本的认证流程。

DaoAuthenticationProvider的用户信息来源于UserDetailsService，并且整合了密码编码的实现，在表单认证就是由DaoAuthenticationProvider提供的。

AuthenticationProvider 是用来 执行具体认证逻辑 的核心接口。
 - AuthenticationProvider 就像一个“认证器”。
 - 它定义了：给定一个 Authentication 对象，我怎么验证它是否合法？
 - 它的主要任务是：
    - 校验用户名/密码是否正确（或者其他类型的凭证）。
    - 返回一个 已认证的 Authentication 对象（带有用户的权限、角色等信息）。
    - 如果认证失败，要么抛出异常，要么返回 null（Spring Security 会继续尝试其他 AuthenticationProvider）。
AuthenticationProvider 只有两个核心方法：
 - authenticate
    1. 真正执行认证的地方。
    2. 参数是未认证的 Authentication（比如 UsernamePasswordAuthenticationToken，里头有用户名和密码）。
    3. 返回已认证的 Authentication（用户名、权限、角色都填好），并且 isAuthenticated 会被设置为 true。
 - supports
    1. 告诉 Spring Security：这个 Provider 支持处理哪种类型的 Authentication。
    2. 比如只处理 UsernamePasswordAuthenticationToken，其他类型交给别的 Provider。

工作流程（简化版）：
    1. 用户提交登录表单，用户名和密码被封装成 UsernamePasswordAuthenticationToken。
    2. ProviderManager（认证管理器）拿着这个 token，找到支持它的 AuthenticationProvider。
    3. 调用 authenticate 方法执行验证逻辑（数据库查用户、验证密码、封装权限）。
    4. 成功就返回已认证的 Authentication，失败就抛出异常（比如 BadCredentialsException）。
    5. Spring Security 用返回的认证对象构造 SecurityContext，后续的请求就知道你是谁、有啥权限。

Spring Security 自带了很多常用的 AuthenticationProvider，比如：
    - DaoAuthenticationProvider：最常用的，验证用户名密码，一般和 UserDetailsService 配合使用。
    - LdapAuthenticationProvider：做 LDAP 验证。
    - JwtAuthenticationProvider：如果你集成了 JWT，可以自己写一个来验证 Token。

Spring 如何装配 DaoAuthenticationProvider 的？
    初始化过程中最终调用 InitializeUserDetailsBeanManagerConfigurer 是会初始化
    首先会寻找是否已经声明了 DaoAuthenticationProvider 类型的 bean，如果没有就会创建一个
也就是说这里创建一个自定义的去改变装配行为，顺带在 additionalAuthenticationChecks 方法中加上图形验证码的校验逻辑
 */

@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(UserDetailsService userDetailsService,
                                    PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        /*
        在这里进行验证码的检测其实不太好，用户状态的检测其实是在这个方法之前的
        在 org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider.authenticate 方法中
        进行完 this.preAuthenticationChecks.check(user) 前置检测后
	    才会进行 additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
	    个人认为还是验证码的校验放到 filter 里去做。
         */
        // MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();
        // String imageCode = details.getImageCode();
        // String savedImageCode = details.getSavedImageCode();
        // // 检验图形验证码
        // if (StringUtils.isEmpty(imageCode) || StringUtils.isEmpty(savedImageCode) || !imageCode.equals(savedImageCode)) {
        //     throw new VerificationCodeException("图形验证码校验失败");
        // }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // 这里拿到额外信息
        DeviceAuthenticationDetails details = (DeviceAuthenticationDetails) token.getDetails();
        String userAgent = details.getUserAgent();
        String ip = details.getRemoteAddress();

        System.out.println("登录用户使用设备: " + userAgent + ", 来自IP: " + ip);

        return super.authenticate(authentication);
    }
}
