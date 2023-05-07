package com.hundsun.demo.springcloud.eureka.zuul.client.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.zuul.client.filter
 * @className: MyFilter
 * @description: 自定义过滤器实现很简单, 只需要继承ZuulFilter, 并实现 ZuulFilter 中的抽象方法, 包括 filterType(), 和 filterOrder(), 以及 IZuulFilter 的 shouldFilter() 和 run() 的两个方法
 * @author: h1123
 * @createDate: 2023/5/7 16:18
 */

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
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            } catch (Exception e) {
            }
            return null;
        }
        log.info("ok");
        return null;
    }
}
