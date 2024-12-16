package org.hulei.javaee.springmvc.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
