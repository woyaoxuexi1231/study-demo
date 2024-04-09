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

这些断言工厂可以组合使用，以实现更复杂的路由规则。您可以根据具体需求在 Spring Cloud Gateway 中选择合适的断言工厂来定义路由规则。


