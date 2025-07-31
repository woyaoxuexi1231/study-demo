package org.hulei.javaee.springmvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * TODO 这里没有生效,不知道是不是需要配置 mvc
 * @author hulei
 * @since 2024/11/13 20:33
 */

@Slf4j
@Component
public class SimpleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("simple filter doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
