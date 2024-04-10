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

这些过滤器提供了各种功能，可以用于修改请求和响应、执行重试、添加安全头等操作。你可以根据需求选择合适的过滤器进行配置。