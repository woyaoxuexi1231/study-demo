package com.hundsun.demo.springcloud.security.filter;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
专门用于处理验证码逻辑的过滤器，在 securityConfig 中进行配置后，添加到 security 过滤器链的合适位置
当匹配到登录请求时，立刻对验证码进行校验，成功则放行，失败则提前结束整个验证请求。

OncePerRequestFilter 是 Spring Framework 提供的过滤器基类，确保一个请求在同一个过滤器链中仅执行一次，避免因重复调用（如 forward、include）导致多次触发。

UsernamePasswordAuthenticationFilter - 处理表单登录（/login POST 请求

Spring Security 过滤器的默认顺序（部分简化）：
 - ChannelProcessingFilter
 - SecurityContextPersistenceFilter
 - ConcurrentSessionFilter
 - CsrfFilter
 - UsernamePasswordAuthenticationFilter
 - BasicAuthenticationFilter
 - RememberMeAuthenticationFilter
 - AnonymousAuthenticationFilter
 - ExceptionTranslationFilter
 - FilterSecurityInterceptor
可通过 http.addFilterBefore() 或 http.addFilterAfter() 调整顺序。
 */
public class CaptchaFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher loginRequest = new AntPathRequestMatcher("/doLogin", "POST");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (loginRequest.matches(request)) {
            String captchaInput = request.getParameter("captcha");
            HttpSession session = request.getSession();
            String captchaExpected = (String) session.getAttribute("captcha");

            if (!StringUtils.hasText(captchaInput) || !captchaInput.equalsIgnoreCase(captchaExpected)) {
                // 验证码不正确，存储错误信息并重定向
                session.setAttribute("error_message", "验证码错误");
                response.sendRedirect("/login-form");
                return;
            }
            // 验证通过后不清除 error_message，避免覆盖其他错误
        }

        filterChain.doFilter(request, response);
    }
}
