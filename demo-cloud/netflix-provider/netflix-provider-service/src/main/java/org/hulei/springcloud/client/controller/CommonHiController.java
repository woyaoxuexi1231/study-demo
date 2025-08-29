package org.hulei.springcloud.client.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.hulei.util.utils.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hulei
 * @since 2025/8/28 16:48
 */

@RequestMapping("/common")
@RestController
public class CommonHiController {

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi/{everything}")
    public void hi(HttpServletRequest req, HttpServletResponse rsp, @PathVariable String everything) {
        // 响应 map
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-sentinel/{everything}")
    public void hiSentinel(HttpServletRequest req, HttpServletResponse rsp, @PathVariable String everything) {
        // 响应 map
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-param/{everything}")
    public void hiParam(@RequestParam("name") String name, HttpServletRequest req, HttpServletResponse rsp, @PathVariable String everything) {
        // 响应 map
        extracted(req, rsp);
    }

    @Value("${server.port}")
    private String port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void extracted(HttpServletRequest req, HttpServletResponse rsp) throws IOException {
        /* 响应 map */
        Map<String, Object> map = new HashMap<>();

        // 参数信息
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            params.put(name, req.getParameter(name));
        }
        map.put("params", params);

        // 返回信息
        String format = String.format("本服务端口：%s，本服务ip：%s，调用者ip：%s%n", port, InetAddress.getLocalHost(), req.getRemoteAddr());
        map.put("format", format);

        // 塞入请求头部
        Map<String, Object> headers = new HashMap<>();
        req.getHeaderNames().asIterator().forEachRemaining((name) -> headers.put(name, req.getHeader(name)));
        map.put("headers", headers);

        map.put("origin", IpUtils.getClientIp(req));

        map.put("url", IpUtils.getFullRequestUrl(req));


        // 判断 Content-Type 是否为 JSON
        String contentType = req.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder jsonBuilder = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
            }
            String jsonBody = jsonBuilder.toString();

            if (!jsonBody.isEmpty()) {
                // 解析 JSON 字符串为 Map
                map.put("jsonBody", objectMapper.readValue(jsonBody, Map.class));
            }
        }

        map.put("http-method", req.getMethod());


        // 响应
        rsp.setContentType("application/json");
        rsp.setCharacterEncoding("UTF-8");
        rsp.getWriter().println(JSON.toJSONString(map));
        rsp.getWriter().flush();
        rsp.getWriter().close();
    }

}
