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