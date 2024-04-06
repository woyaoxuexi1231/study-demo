### spring cloud和dubbo的一些区别

| 特性           | Spring Cloud                                        | Dubbo                                           |
|----------------|-----------------------------------------------------|-------------------------------------------------|
| 框架类型       | 基于 Spring Framework 构建的微服务框架               | 阿里巴巴开源的分布式服务框架                     |
| 生态系统       | 与 Spring 生态系统集成，适合于 Spring 应用开发      | 独立的分布式服务框架，可以与任何 Java 框架集成 |
| 服务注册与发现 | 使用 Eureka、Consul、Zookeeper 等注册中心          | 使用 Zookeeper、Nacos 等注册中心                |
| 通信协议       | 基于 HTTP 协议，使用 Ribbon 或 Feign 进行通信     | 基于 RPC（Remote Procedure Call）协议           |
| 负载均衡       | 使用 Ribbon 或 Spring Cloud LoadBalancer           | 内置负载均衡策略，也可自定义                   |
| 服务调用       | 使用 Feign、RestTemplate 等                         | 使用 Dubbo RPC 协议、Dubbo Proxy 等            |
| 容错处理       | 使用 Hystrix、Resilience4j 等                       | 内置容错机制，如熔断、限流、降级等            |
| 分布式配置     | 使用 Spring Cloud Config                           | 可以使用 Zookeeper、Nacos 等进行配置管理      |
| 分布式事务     | 通常使用 Spring Cloud Sleuth 和 Zipkin 进行追踪     | 通过 Dubbo TCC（Try-Confirm-Cancel）等实现     |

