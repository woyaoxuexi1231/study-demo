1. **eureka-server**

   注册中心服务,提供服务注册的能力,是一个必须启动的服务
2. **eureka-client**

   一个简单的使用eureka-client依赖的服务提供者,示例只是简单的向注册中心注册了eureka-client这个简单的服务而已

3. ~~**eureka-ribbon-client**~~

   负载均衡组件，通常和eureka、zuul、restTemplate、feign配合使用。ribbon和zuul配合，很容易做到负载均衡。

   但是在新版本，Netflix 已经宣布停止对 Ribbon 库的维护，Spring Cloud 项目推出了更现代化、更灵活的替代方案 Spring Cloud LoadBalancer

4. **spring-cloud-loadbalancer**

   Spring Cloud LoadBalancer 是 Spring Cloud 中的一个模块，用于在微服务架构中实现负载均衡。在微服务架构中，通常会有多个服务实例提供相同的服务，负载均衡器的作用是根据一定的策略从这些实例中选择一个来处理请求，以实现负载均衡和高可用性。

   Spring Cloud LoadBalancer 提供了一种轻量级的客户端负载均衡解决方案，它与 Spring Cloud 中的服务发现组件（如 Netflix Eureka）集成，可以动态地从服务注册中心获取服务实例的信息，并根据特定的负载均衡策略选择合适的服务实例。Spring
   Cloud LoadBalancer 还支持自定义的负载均衡策略，以满足不同场景下的需求。

   使用 Spring Cloud LoadBalancer，开发者可以很方便地实现客户端负载均衡，而无需依赖于特定的负载均衡实现（如 Ribbon）。这使得应用程序更加灵活，并且更容易在不同的环境中部署和迁移。

5. ~~**eureka-zuul-client**~~

   `spring-cloud-starter-netflix-zuul` 是 Spring Cloud 中的一个模块，用于集成 Netflix Zuul 到 Spring Cloud 应用程序中。它提供了简化配置和依赖管理的功能，使得在 Spring Cloud 应用中使用 Zuul
   作为服务网关更加方便。

   具体来说，`spring-cloud-starter-netflix-zuul` 模块允许你通过简单的配置将 Zuul 集成到你的 Spring Cloud 应用中，从而实现请求的路由、过滤、负载均衡等功能。通过
   Zuul，你可以轻松地构建一个弹性和高可用性的服务网关，用于处理外部请求并将其路由到后端的微服务上。

   这个模块依赖于 Spring Cloud Netflix 子项目，它是 Spring Cloud 生态系统中的一部分，用于集成 Netflix 开源组件到 Spring Cloud 应用中。使用 `spring-cloud-starter-netflix-zuul`，你可以方便地在
   Spring Cloud 应用中使用 Zuul，从而实现更加灵活和可扩展的微服务架构。

6. **eureka-feign-client**

   `spring-cloud-starter-openfeign` 是 Spring Cloud 中的一个模块，用于简化基于 Feign 的服务间通信的开发。Feign 是一个声明式的、模板化的 HTTP 客户端，它使得编写基于 RESTful 服务的客户端变得更加简单和优雅。

   `spring-cloud-starter-openfeign` 模块封装了 Feign 客户端的依赖，并提供了与 Spring Cloud 的集成，使得在使用 Feign 进行服务间通信时更加方便。通过使用 Feign，你可以将远程服务的调用当作是本地方法的调用来处理，而无需手动处理
   HTTP 请求和响应。

   以下是 `spring-cloud-starter-openfeign` 主要提供的功能和特性：

    1. **声明式的服务调用**：通过定义接口并添加 `@FeignClient` 注解，可以声明式地定义远程服务的调用方式，而不需要手动编写 HTTP 请求和响应的处理逻辑。

    2. **集成 Ribbon**：Feign 默认集成了 Ribbon 负载均衡器，可以通过配置和注解的方式来实现服务的负载均衡调用。

    3. **集成 Hystrix**：Feign 默认集成了 Hystrix 断路器，可以通过配置和注解的方式来实现服务的容错处理，包括超时设置、重试机制等。

    4. **支持自定义配置**：可以通过配置文件或者 Java 代码来对 Feign 客户端进行配置，例如超时时间、连接池大小等。

    5. **支持多种编码器和解码器**：Feign 支持多种编码器和解码器，包括 JSON、XML 等，可以根据实际需求选择合适的数据格式。

    6. **支持拦截器**：Feign 支持拦截器机制，可以通过拦截器来对请求和响应进行预处理和后处理。

   通过使用 `spring-cloud-starter-openfeign`，你可以更加方便地开发和管理基于 Feign 的服务间通信，从而使得微服务架构下的服务间通信变得更加简单和高效。

7. ~~**eureka-monitor-client~~**

   `spring-cloud-starter-netflix-turbine` 是 Spring Cloud 中的一个模块，用于构建聚合 Hystrix 指标流的监控仪表板。Turbine 将多个微服务的 Hystrix 指标聚合到一个流中，使得你可以在一个集中的地方监控整个微服务架构的状况。

   以下是 `spring-cloud-starter-netflix-turbine` 主要提供的功能和特性：

    1. **聚合 Hystrix 指标流**：Turbine 收集各个微服务中的 Hystrix 指标流，并将它们聚合成一个流，以便进行监控和分析。

    2. **多种聚合方式**：Turbine 支持多种聚合方式，包括按照集群、按照应用程序实例、按照集群和实例等，你可以根据实际情况选择合适的聚合方式。

    3. **集成 Hystrix Dashboard**：Turbine 可以集成 Hystrix Dashboard，将聚合后的指标流展示在仪表板上，方便实时监控微服务的状况。

    4. **支持可视化监控**：通过 Turbine，你可以以图形化的方式监控整个微服务架构的状况，包括请求次数、失败次数、超时次数、断路器状态等。

    5. **支持多种数据源**：Turbine 支持多种数据源，包括 Eureka、Zookeeper、Consul 等，你可以根据实际情况选择合适的数据源。

   通过使用 `spring-cloud-starter-netflix-turbine`，你可以方便地构建和部署 Hystrix 指标流的聚合监控系统，从而实现对整个微服务架构的实时监控和分析。这对于实时发现和解决微服务架构中的问题，提高系统的稳定性和可靠性非常有帮助。

