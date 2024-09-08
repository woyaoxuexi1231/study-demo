## 服务网关 Gateway

spring cloud 官方推出的第二代网关,用于替代 zuul,不仅提供了统一的路由方式,还提供了Filter链式网关的功能

1. 协议转换,路由转发
2. 流量聚合,对流量进行监控,日志输出
3. 作为整个系统的前端工程,对流量进行控制,有限流的作用
4. 作为网关层的权限判断
5. 可以在网关层做缓存

zuul有两大组件,routing和filter<br>
和zuul一样,gateway也有这两个组件,但是多了一个断言工厂(Predicate),断言用于判断请求具体交给哪一个 Gateway Web Handler
处理,处理时会经过一系列的过滤器链


> http://localhost:12020/eureka-client/hi<br>
> 由于我们自己创建了一个TokenGlobalFilter,所有header里面需要添加Cookie才行  Cookie=token=1