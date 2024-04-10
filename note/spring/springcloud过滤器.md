Spring Cloud 中的过滤器工厂可以用于定义和配置 Zuul 或 Gateway 等网关的过滤器。以下是 Spring Cloud 中一些常用的内置过滤器工厂：

1. **AddRequestHeaderHeader**：添加请求头的过滤器工厂。
2. **AddResponseHeaderHeader**：添加响应头的过滤器工厂。
3. **PrefixPath**：为请求添加前缀路径的过滤器工厂。
4. **PreserveHostHeader**：保留原始请求中的主机头的过滤器工厂。
5. **RequestRateLimiter**：请求速率限制器的过滤器工厂。
6. **RemoveRequestHeader**：移除请求头的过滤器工厂。
7. **RemoveResponseHeader**：移除响应头的过滤器工厂。
8. **RewritePath**：重写请求路径的过滤器工厂。
9. **RewriteResponseHeader**：重写响应头的过滤器工厂。
10. **Retry**：重试请求的过滤器工厂。
11. **SetPath**：设置请求路径的过滤器工厂。
12. **SetRequestHeader**：设置请求头的过滤器工厂。
13. **SetResponseHeader**：设置响应头的过滤器工厂。
14. **StripPrefix**：去除请求前缀的过滤器工厂。
15. **FallbackHeaders**：降级处理的过滤器工厂，用于添加回退响应头。

这些过滤器工厂可以通过配置文件或代码来使用，并且可以组合使用以实现更复杂的路由和过滤逻辑。具体使用哪些过滤器工厂取决于你的项目需求和设计。