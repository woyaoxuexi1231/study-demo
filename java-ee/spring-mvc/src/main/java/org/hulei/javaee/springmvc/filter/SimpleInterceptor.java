package org.hulei.javaee.springmvc.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author hulei
 * @since 2023/4/12 23:39
 */

@Slf4j
public class SimpleInterceptor implements HandlerInterceptor {

    /**
     * 该方法在执行控制器方法之前执行. 返回 false 表示拦截请求, 不再往下执行; 返回 true 表示放行, 继续执行.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("SimpleInterceptor preHandle, method: {}", request.getMethod());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 该方法在执行控制器之后执行. 且在返回 ModelAndView 之前执行
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      the handler (or {@link HandlerMethod}) that started asynchronous
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // log.info("拦截器方法: SimpleFilter - postHandle");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 该方法在执行完控制器之后执行
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // log.info("拦截器方法: SimpleFilter - afterCompletion");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
