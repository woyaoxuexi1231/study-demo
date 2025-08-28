package org.hulei.springcloud.client.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.hulei.springcloud.client.IpUtils;
import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.client.controller
 * @className: HiController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 22:53
 */

// @RequestMapping("/client")
@RestController
@Slf4j
public class HiController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${server.port}")
    private String port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi")
    public void hi(HttpServletRequest req, HttpServletResponse rsp) {
        // 响应 map
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-default-rate-limit")
    public void hiDefaultRateLimit(HttpServletRequest req, HttpServletResponse rsp) {
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-default-rate-limit2")
    public void hiDefaultRateLimit2(HttpServletRequest req, HttpServletResponse rsp) {
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-custom-local-ratelimit")
    public void hiCustomLocalRatelimit(HttpServletRequest req, HttpServletResponse rsp) {
        extracted(req, rsp);
    }

    @DoneTime
    @SneakyThrows
    @RequestMapping("/hi-custom-redis-ratelimit")
    public void hiCustomRedisRatelimit(HttpServletRequest req, HttpServletResponse rsp) {
        extracted(req, rsp);
    }

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


    @GetMapping("/hi-request-param")
    @SneakyThrows
    public String hiWithRequestParam(@RequestParam(required = false, value = "param") String param, HttpServletRequest req, HttpServletResponse rsp) {
        /*
        参数附加在 URL 末尾，以 ?key=value 形式传递，多个参数用 & 分隔。

        后端接收方式（Spring MVC）：
            使用 @RequestParam 注解绑定参数到方法参数（支持默认值、必填校验）

        适用场景：
            过滤、排序、分页等轻量级查询（如商品列表筛选）。
            参数数量少且无需保密（暴露在 URL 中）。
         */
        String format = String.format("本服务端口：%s，本服务ip：%s，调用者ip：%s，调用传递的URL 查询参数（Query Parameters）是：%s",
                port, InetAddress.getLocalHost(), req.getRemoteAddr(), param);
        log.info(format);
        return format;
    }

    @GetMapping(path = {"/hi-path-param/{param}"}, name = "Restful 风格的 URL 请求")
    @SneakyThrows
    public String hiWithPathParam(@PathVariable("param") String param, HttpServletRequest req, HttpServletResponse rsp) {
        /*
        参数直接嵌入 URL 路径中，通过占位符（如 {param}）标识，用于定位具体资源。

        后端接收方式（Spring MVC）：
            使用 @PathVariable 注解绑定路径中的变量到方法参数

        适用场景：
            资源定位（如根据用户 ID 查询其订单）。
            需要暴露在 URL 中的关键标识（如短链接、分类路径 /products/electronics/phones）。
         */
        String format = String.format("本服务端口：%s，本服务ip：%s，调用者ip：%s，调用传递的路径参数（Path Variables）是：%s",
                port, InetAddress.getLocalHost(), req.getRemoteAddr(), param);
        log.info(format);
        return format;
    }

    @GetMapping("/hi-databinding")
    @SneakyThrows
    public String hiDataBinding(SimpleReqDTO reqDTO, HttpServletRequest req, HttpServletResponse rsp) {
        /*
        GET 请求的参数只能通过 URL 查询字符串传递（HTTP 规范不限制，但实际中浏览器/服务器通常忽略请求体）。
        Spring MVC 处理 GET 方法的对象参数时，核心是 数据绑定（Data Binding）。

        解析流程：
            1. Spring 使用 RequestParamMethodArgumentResolver 处理无注解的对象参数，但实际绑定逻辑由 WebDataBinder 完成。
            2. 一般经历 属性名匹配 -> 类型转换
         */
        String format = String.format("本服务端口：%s，本服务ip：%s，调用者ip：%s，数据绑定后的结果是：%s",
                port, InetAddress.getLocalHost(), req.getRemoteAddr(), reqDTO);
        log.info(format);
        return format;
    }

    @PostMapping("/hi-body")
    @SneakyThrows
    public String hiBody(@RequestBody SimpleReqDTO reqDTO, HttpServletRequest req, HttpServletResponse rsp) {
        /*
        POST 请求的参数通常放在请求体中（HTTP 规范允许，但实际中主流场景如此）。
        当使用 @RequestBody 注解时，Spring MVC 处理对象参数的核心是 消息转换器反序列化。

        参数来源与解析流程
            - 所有参数来自 HTTP 请求体（Body），格式由 Content-Type 头决定（如 application/json、application/xml）。
            - 解析器选择：Spring 根据 Content-Type 选择对应的 HttpMessageConverter（如 MappingJackson2HttpMessageConverter 处理 JSON）。
            流程：
             1. 读取请求体：通过 ServletInputStream 读取请求体的原始字节数据（如 JSON 字符串）。
             2. 消息转换：调用 HttpMessageConverter 的 read() 方法，将字节数据反序列化为 SimpleReqDTO 对象（依赖 Jackson、JAXB 等库）。
             3. 验证与绑定：若对象包含校验注解（如 @NotBlank），Spring 会自动触发校验（需配合 @Valid 注解）。
         */
        String format = String.format("本服务端口：%s，本服务ip：%s，调用者ip：%s，请求体中的参数解析为：%s",
                port, InetAddress.getLocalHost(), req.getRemoteAddr(), reqDTO);
        log.info(format);
        return format;
    }

    @PostMapping("/change")
    @SneakyThrows
    public Map<String, Object> change(@RequestBody Map<String, Object> map, HttpServletRequest req, HttpServletResponse rsp) {
        /*

         */
        return Map.of();
    }

    public Map<String, Object> change(@RequestBody Map<String, Object> map) {
        // 设置时区为东八区（北京时间）
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        // 获取当前时间
        Date now = new Date();
        // 根据指定时区获取当前时间
        Date nowInTimeZone = new Date(now.getTime() + timeZone.getRawOffset());
        map.put("response-tag", nowInTimeZone);
        return map;
    }
}
