package org.hulei.springboot.spring.mvc.filter.webfilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author hulei
 * @since 2024/11/13 20:40
 */

@WebFilter(urlPatterns = {"/get-ip"})
// @Component
@Slf4j
public class IpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("{} : {}", IpFilter.class.getSimpleName(), ((HttpServletRequest) (servletRequest)).getServletPath());
        getClientIp((HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 获取客户端的真实IP地址
     *
     * @param request HttpServletRequest对象
     * @return 客户端的真实IP地址，如果无法获取到，则返回null
     */
    public static String getClientIp(HttpServletRequest request) {
        String ipAddress = null;
        try {

            log.info("X-Forwarded-For : {}", request.getHeader("X-Forwarded-For"));
            log.info("X-Forwarded-Host : {}", request.getHeader("X-Forwarded-Host"));
            log.info("X-Forwarded-Port : {}", request.getHeader("X-Forwarded-Port"));
            log.info("proxy-client-ip : {}", request.getHeader("proxy-client-ip"));
            log.info("wl-proxy-client-ip : {}", request.getHeader("wl-proxy-client-ip"));
            log.info("http_client_ip : {}", request.getHeader("http_client_ip"));
            log.info("http_x_forwarded_for : {}", request.getHeader("http_x_forwarded_for"));
            log.info("getRemoteAddr : {}", request.getRemoteAddr());


            // 依次检查不同的HTTP头字段
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("x-real-ip");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("proxy-client-ip");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("wl-proxy-client-ip");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("http_client_ip");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("http_x_forwarded_for");
            }
            // 如果以上HTTP头字段都没有获取到IP地址，则尝试获取直接连接的IP地址
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }

            // 对于"X-Forwarded-For"字段，可能存在多个IP地址，通常第一个IP地址是客户端的真实IP地址
            if (ipAddress != null && ipAddress.contains(",")) {
                ipAddress = ipAddress.split(",")[0].trim();
            }
        } catch (Exception e) {
            ipAddress = null; // 如果出现异常，则返回null
        }
        return ipAddress;
    }
}
