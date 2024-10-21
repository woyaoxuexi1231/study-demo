## 智能路由网关 zuul

Netflix构建微服务的组件之一 - 智能路由网关, 致力于动态路由,过滤,监控,弹性伸缩和安全

1. zuul,ribbon,eureka三者结合,可实现智能路由和负载均衡的功能
2. 网关将所有服务的api统一聚合,并统一向外界暴露
3. 网关服务可以做身份认证和权限认证
4. 网关可以实现监控功能,实时输出日志,对请求进行记录
5. 网关可以监控流量,在高流量情况下,对服务进行降级

zuul通过servlet来实现,zuul通过自定义的zuulServlet(类似于DispatcherServlet)来对请求进行控制

- pre过滤器
- routing过滤器
- post过滤器
- error过滤器

当一个进入zuul网关时, 先进入pre filter进行一系列的验证,操作和判断,
然后交给routing filter进行路由转发,转发到具体的服务实例进行逻辑处理,返回数据
最后由post filter进行处理,该类型的处理器处理完之后,将response信息返回给客户端

**zuulServlet**作为zuul的核心,作用是编排和初始化所有的zuulFilter,通过service()方法执行过滤器的逻辑

本例中可以使用下面api访问接口
> http://localhost:12014/v1/hiapi/hi?token=xxx
> http://localhost:12014/v1/ribbonapi/hi?token=xxx

## 服务网关 Gateway

spring cloud 官方推出的第二代网关,用于替代 zuul,不仅提供了统一的路由方式,还提供了Filter链式网关的功能

1. 协议转换,路由转发
2. 流量聚合,对流量进行监控,日志输出
3. 作为整个系统的前端工程,对流量进行控制,有限流的作用
4. 作为网关层的权限判断
5. 可以在网关层做缓存

### spring cloud gateway 断言工厂

断言工厂用于判断请求是否符合某些条件，从而决定请求是否应该路由到特定的服务。
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

### spring cloud gateway 过滤器

过滤器用于对请求或响应进行处理，拦截并修改传入的请求或传出的响应。
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

zuul有两大组件,routing和filter<br>
和zuul一样,gateway也有这两个组件,但是多了一个断言工厂(Predicate),断言用于判断请求具体交给哪一个 Gateway Web Handler
处理,处理时会经过一系列的过滤器链


> http://localhost:12020/eureka-client/hi<br>
> 由于我们自己创建了一个TokenGlobalFilter,所有header里面需要添加Cookie才行 Cookie=token=1

