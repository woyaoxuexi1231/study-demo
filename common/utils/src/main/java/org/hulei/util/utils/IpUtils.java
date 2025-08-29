package org.hulei.util.utils;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {

    /**
     * 获取请求的客户端真实 IP 地址
     *
     * @param request HttpServletRequest
     * @return 真实 IP，如果无法获取则返回请求的远程地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = null;

        // 1. X-Forwarded-For: 多级代理情况下，第一个 IP 通常是客户端真实 IP
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 可能有多个 IP，用逗号分隔，第一个为客户端真实 IP
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            ip = ip.trim();
            if (!"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }

        // 2. X-Real-IP：Nginx 等代理常见字段
        ip = request.getHeader("X-Real-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 3. 其他可能的代理头（根据实际代理配置调整）
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("WL-Proxy-Client-IP"); // WebLogic
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }

        // 4. 如果以上都获取不到，使用 getRemoteAddr()
        ip = request.getRemoteAddr();
        return ip != null ? ip : "unknown";
    }

    /**
     * 获取当前请求的完整 URL（包含协议、域名、端口、路径、参数）
     * 例如：https://example.com:8080/user/info?id=123
     *
     * @param request HttpServletRequest
     * @return 完整的请求 URL
     */
    public static String getFullRequestUrl(HttpServletRequest request) {
        if (request == null) {
            return "";
        }

        StringBuilder url = new StringBuilder();

        // 1. 协议 (http / https)
        String scheme = request.getScheme();             // http 或 https
        url.append(scheme).append("://");

        // 2. 服务器名称 (域名或IP)
        url.append(request.getServerName());             // example.com

        // 3. 端口 (如果不是默认的 http 80 / https 443，则需要加上)
        int port = request.getServerPort();
        if (!(("http".equals(scheme) && port == 80) ||
                ("https".equals(scheme) && port == 443))) {
            url.append(":").append(port);                // :8080
        }

        // 4. 请求 URI（包含路径和查询参数，如 /user/info?id=123）
        String uri = request.getRequestURI();            // 如 /user/info
        String queryString = request.getQueryString();   // 如 id=123
        if (queryString != null) {
            uri = uri + "?" + queryString;
        }

        url.append(uri);

        return url.toString();
    }
}