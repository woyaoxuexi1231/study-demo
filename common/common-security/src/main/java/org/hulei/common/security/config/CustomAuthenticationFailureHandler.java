package org.hulei.common.security.config;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
AuthenticationFailureHandler 用于定义认证失败后的处理逻辑
 - 重定向到错误页面
 - 返回 JSON 响应（适用于前后端分离）
 - 记录日志或触发其他业务逻辑

SimpleUrlAuthenticationFailureHandler
继承 AuthenticationFailureHandler，提供基于 URL 重定向的失败处理，是 Spring Security 的默认实现。
 - 重定向到指定页面，默认跳转到登录页并附加 ?error 参数（如 /login?error）。
 - 支持自定义错误页面，可通过 setDefaultFailureUrl("/login-fail") 修改目标 URL。
 - 支持 RedirectStrategy，允许控制重定向行为（如是否自动添加 contextPath，或处理 AJAX 请求）。


需要简单重定向 → 直接用 SimpleUrlAuthenticationFailureHandler。
需要复杂逻辑（如 JSON 响应）→ 自定义实现 AuthenticationFailureHandler。
 */
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMsg;

        if (exception instanceof LockedException) {
            errorMsg = "你的账户已被锁定！";
        } else if (exception instanceof DisabledException) {
            errorMsg = "你的账户当前不可用！";
        } else if (exception instanceof AccountExpiredException) {
            errorMsg = "你的账户已过期！";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMsg = "您的凭据已过期！";
        } else if (exception instanceof BadCredentialsException) {
            errorMsg = "账户名或密码错误！";
        } else {
            errorMsg = exception.getMessage();
        }

        // 把具体消息放到 session
        System.out.println(errorMsg);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
