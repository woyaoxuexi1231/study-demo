package com.hundsun.demo.springcloud.gateway;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2023/5/5 20:44
 */

@RestController
@EnableDiscoveryClient
@Slf4j
@SpringBootApplication
public class GatewayApplication {

    static ApplicationContext applicationContext;

    public static void main(String[] args) {

        /*
        Spring Cloud Gateway 是基于 Spring WebFlux 和 Reactor 模型构建的一个异步非阻塞 API 网关
        它的核心功能是：请求路由、过滤器链处理、服务发现和负载均衡等。

        💡断言工厂！
        断言工厂用于判断一个请求是否符合某个条件，即决定一个请求是否应该被当前的路由规则匹配到。
        换句话说：断言就是“条件判断”，用来决定这个请求走不走这个路由。
        断言是基于传入的请求信息（如请求路径、请求头、请求参数、HTTP 方法等）来做出路由决策的。
        Spring Cloud Gateway内置了多种断言工厂（Predicate Factory），用于匹配请求的特定条件。以下是一些常见的内置断言工厂：
            1. `AfterRoutePredicateFactory`: 基于路由后的时间进行匹配的断言工厂。匹配不上会路由到指定路由
            2. `BeforeRoutePredicateFactory`: 基于路由前的时间进行匹配的断言工厂。匹配不上会路由到指定路由
            3. `BetweenRoutePredicateFactory`: 在指定时间范围内进行匹配的断言工厂。匹配不上会路由到指定路由
            4. `CookieRoutePredicateFactory`: 匹配请求中特定的 Cookie 的断言工厂。匹配上才路由到指定地址,否则404
            5. `HeaderRoutePredicateFactory`: 匹配请求中特定的 Header 的断言工厂。匹配上才路由到指定地址,否则404
            6. `HostRoutePredicateFactory`: 匹配请求的 Host 的断言工厂。匹配上才路由到指定地址,否则404
            7. `MethodRoutePredicateFactory`: 匹配请求的 HTTP 方法的断言工厂。匹配上才路由到指定地址,否则404
            8. `PathRoutePredicateFactory`: 匹配请求的路径的断言工厂。
            9. `QueryRoutePredicateFactory`: 匹配请求的查询参数的断言工厂。
            10. `RemoteAddrRoutePredicateFactory`: 匹配请求的远程地址的断言工厂。

        💡过滤器！
        过滤器工厂用于对请求和响应进行加工处理，比如添加/删除请求头、修改请求体、鉴权、限流、日志记录、重试等。
        换句话说：过滤器就是“处理逻辑”，用来在请求被转发前后做些额外的事情。
        过滤器执行的是在请求被路由前或响应返回前的某些额外逻辑，比如身份认证、日志记录、负载均衡、熔断等。
        Spring Cloud Gateway 内置了许多过滤器，用于处理请求和响应。以下是一些常见的内置过滤器：
            1. **ForwardRoutingFilter**：用于转发请求到目标服务。
            2. **ForwardPathFilter**：用于处理转发路径。
            3. **AddRequestHeaderFilter**：用于添加请求头。
            4. **AddResponseHeaderFilter**：用于添加响应头。
            5. **RemoveRequestHeaderFilter**：用于移除请求头。
            6. **RemoveResponseHeaderFilter**：用于移除响应头。
            7. **PrefixPathFilter**：用于添加或移除路径前缀。
            8. **SetPathFilter**：用于设置路径。
            9. **SetStatusFilter**：用于设置响应状态码。
            10. **RetryGatewayFilterFactory**：用于执行重试操作。
            11. **RewritePathGatewayFilterFactory**：用于重写请求路径。
            12. **RewriteResponseHeaderGatewayFilterFactory**：用于重写响应头。
            13. **SetRequestHeaderGatewayFilterFactory**：用于设置请求头。
            14. **SetResponseHeaderGatewayFilterFactory**：用于设置响应头。
            15. **SecureHeadersGatewayFilterFactory**：用于添加安全相关的响应头，如X-Content-Type-Options、X-XSS-Protection等。
            16. **RedirectToGatewayFilterFactory**：用于执行重定向操作。
            17. **AddRequestParameterGatewayFilterFactory**：用于添加请求参数。
            18. **RemoveRequestParameterGatewayFilterFactory**：用于移除请求参数。
            19. **MapRequestHeaderGatewayFilterFactory**：用于映射请求头。
            20. **MapResponseHeaderGatewayFilterFactory**：用于映射响应头。
            21. **HystrixGatewayFilterFactory**：用于执行 Hystrix 断路器操作。

        💻 断言工厂决定“哪些请求会走到这个路由”（匹配条件），过滤器工厂决定“对这个请求或响应做哪些处理”（增强逻辑）。



         */
        applicationContext = SpringApplication.run(GatewayApplication.class, args);
    }

    @SentinelResource(value = "yourApi",
            blockHandler = "handleBlock")
    @GetMapping("/hi")
    public String hi(@RequestParam("name") String name) {
        return "hello " + name;
    }

    // blockHandler 方法
    public String handleBlock(String name, BlockException ex) {
        return "hi 接口被限流了！";
    }

}
