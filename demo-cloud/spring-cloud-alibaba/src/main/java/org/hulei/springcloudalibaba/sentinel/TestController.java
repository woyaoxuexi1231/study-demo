package org.hulei.springcloudalibaba.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hulei.util.dto.SimpleReqDTO;
import org.hulei.util.utils.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    // 示例接口：模拟一个可能被限流的接口
    @GetMapping("/hello")
    @SentinelResource(
            value = "hello",                      // 资源名称，用于 Sentinel 控制台定义规则
            blockHandler = "handleBlock",         // 限流/熔断时的处理方法
            fallback = "handleFallback"           // 业务异常或 fallback 处理（非 Sentinel 规则触发）
    )
    public String hello() {
        // 模拟业务逻辑
        return "Hello, Sentinel!";
    }

    // 限流或熔断时进入此方法（必须为 public，参数与原方法一致 + BlockException），如果参数不一致会报错 java.lang.reflect.UndeclaredThrowableException
    public String handleBlock(BlockException ex) {
        return "请求被限流或熔断了，请稍后再试！(BlockException)";
    }

    // 业务异常或 fallback（非 Sentinel 触发，比如代码抛异常时）
    public String handleFallback(Throwable t) {
        return "服务降级处理，出现了异常：" + t.getMessage();
    }

    // 另一个示例：直接使用默认的 fallback 或 blockHandler 方法名
    @GetMapping("/test")
    @SentinelResource(
            value = "testResource",
            blockHandler = "blockHandlerForTest",
            fallback = "fallbackForTest"
    )
    public String test() {
        // 模拟偶尔会出错的业务
        if (Math.random() > 0.5) {
            throw new RuntimeException("随机异常");
        }
        return "这是测试接口";
    }

    // blockHandler 方法
    public String blockHandlerForTest(BlockException ex) {
        return "test 接口被限流了！";
    }

    // fallback 方法
    public String fallbackForTest(Throwable t) {
        return "test 接口出错了，降级返回：" + t.getMessage();
    }


    @Value(value = "${server.port}")
    private String port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SentinelResource(
            value = "hi-body",
            blockHandler = "blockHandlerForHi"
    )
    @PostMapping("/hi-body")
    @SneakyThrows
    public void hiBody(@RequestBody SimpleReqDTO reqDTO, HttpServletRequest req, HttpServletResponse rsp) {
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

        map.put("jsonBody", reqDTO);

        map.put("http-method", req.getMethod());


        // 响应
        rsp.setContentType("application/json");
        rsp.setCharacterEncoding("UTF-8");
        rsp.getWriter().println(JSON.toJSONString(map));
        rsp.getWriter().flush();
        rsp.getWriter().close();
    }

    public void blockHandlerForHi(SimpleReqDTO reqDTO, HttpServletRequest req, HttpServletResponse rsp, BlockException ex) {
        log.info("hiBody 接口被限流了！开始进行一些处理");
    }


}