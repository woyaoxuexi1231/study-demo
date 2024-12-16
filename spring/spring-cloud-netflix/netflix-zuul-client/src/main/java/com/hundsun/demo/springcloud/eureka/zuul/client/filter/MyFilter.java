package com.hundsun.demo.springcloud.eureka.zuul.client.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * ZuulFilter 是 Netflix Zuul 中的一个重要组件，用于在请求的生命周期中执行过滤操作。它可以用来实现各种功能，例如请求路由、请求鉴权、请求日志、请求监控等。通过编写自定义的 ZuulFilter，你可以在请求经过 Zuul 网关时对请求进行修改、记录、拦截或者根据一定的条件进行处理。
 * <p>
 * Zuul 中的过滤器可以分为四种类型：
 * <p>
 * Pre Filter（前置过滤器）：在请求路由之前执行，用于对请求进行预处理，例如鉴权、添加请求头等。
 * <p>
 * Route Filter（路由过滤器）：在请求路由到后端服务之前执行，用于将请求发送到对应的后端服务。
 * <p>
 * Post Filter（后置过滤器）：在请求路由到后端服务并获得响应之后执行，用于对响应进行处理，例如添加响应头、记录响应日志等。
 * <p>
 * Error Filter（错误过滤器）：处理在请求路由过程中发生的错误。
 * <p>
 * 你可以根据自己的需求编写自定义的 ZuulFilter，并根据其类型和顺序在 Zuul 网关中进行注册。通过使用 ZuulFilter，你可以对请求进行灵活的控制和处理，实现一些自定义的功能，如请求转发、权限控制、日志记录、限流等。
 *
 * @author h1123
 * @since 2023/5/7 16:18
 */

@Deprecated
@Slf4j
@Component
public class MyFilter extends ZuulFilter {


    /**
     * filterType()即过滤器的类型, 有 4 种类型，分别是 pre, post, routing 和 error
     *
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * filterOrder() 是过滤顺序, 它为一个 Int 类型的值, 值越小, 越早执行该过滤器
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * shouldFilter() 表示该过滤器是否过滤逻辑, 如果为 true, 则执行 run() 方法; 如果为 false, 则不执行 run() 方法
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run() 方法写具体的过滤的逻辑
     * 在本例中, 检查请求的参数中是否传了 token 这个参数, 如果没有传, 则请求不被路由到具体的服务实例, 直接返回响应, 状态码为 401
     *
     * @return
     */
    @Override
    public Object run() {
        // 获取当前请求的上下文对象，它包含了请求和响应的信息。
        RequestContext ctx = RequestContext.getCurrentContext();
        // 从上下文中获取当前请求对象，以便后续对请求进行处理。
        HttpServletRequest request = ctx.getRequest();
        // 记录当前请求的方法和请求URL，用于日志记录。
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        // 从请求参数中获取名为 "token" 的参数值，用于鉴权。
        Object accessToken = request.getParameter("token");
        // 如果 token 参数为空，则表示未通过鉴权，进行以下处理：
        if (accessToken == null) {
            log.warn("token is empty");
            // 禁止将请求转发到后端服务。
            ctx.setSendZuulResponse(false);
            // 设置响应状态码为 401（未授权）。
            ctx.setResponseStatusCode(401);
            try {
                // 尝试将 "token is empty" 写入响应体，以便返回给客户端。
                ctx.getResponse().getWriter().write("token is empty");
            } catch (Exception e) {
            }
            return null;
        }
        log.info("ok");
        // 表示过滤器的执行结束，不对请求做任何进一步的处理。
        return null;
    }
}
