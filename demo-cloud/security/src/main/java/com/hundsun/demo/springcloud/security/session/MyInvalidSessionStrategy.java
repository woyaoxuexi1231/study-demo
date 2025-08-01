package com.hundsun.demo.springcloud.security.session;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author hulei
 * @since 2025/8/1 0:19
 */

public class MyInvalidSessionStrategy implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /*
        自定义的 InvalidSessionStrategy，session失效的处理策略
        在不配置 remember-me 以及长时间不操作导致 session 失效的情况下，这个策略会触发(需要在 HttpSecurity 配置)
         */
        request.getSession().setAttribute("error_message", "session 已过期");
        response.sendRedirect("/login-form");
    }
}
