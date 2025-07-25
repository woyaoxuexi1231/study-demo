package com.hundsun.demo.springcloud.security.authenticationprovider;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/*
AuthenticationDetailsSource 在表单登录时，允许你在 UsernamePasswordAuthenticationFilter 捕获到用户名密码时，额外收集一些自定义信息，并把这些信息注入到 Authentication 流程里。

简单来说：
 - 正常情况下，Spring Security 登录只拿 username + password。
 - 如果你还需要从请求中拿别的东西（比如：验证码、IP 地址、客户端信息、来源渠道、自定义参数），就需要用 authenticationDetailsSource，把这些额外信息放到 WebAuthenticationDetails 的子类里。

最典型的就是 图形验证码：
    在表单登录时，你需要拿到：username，password，验证码（captcha）
    但默认的 UsernamePasswordAuthenticationFilter 只会取 username 和 password。
    如果你要用 Provider 在认证时比对验证码（而不是在 Filter 里自己写逻辑），就必须把验证码放到 details 里。

它和自定义 Filter 有什么区别？
 - 如果在 Filter 比对验证码，你得在过滤器里直接干预 response，直接拦截。
 - 如果放在 Provider，通过 authenticationDetailsSource 把验证码传给 Provider，那么 Filter 只负责采集，校验放到统一的认证 Provider ➜ 更优雅，更 Spring Security 的方式。
 */

@Component
public class DeviceAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new DeviceAuthenticationDetails(request);
    }

}
