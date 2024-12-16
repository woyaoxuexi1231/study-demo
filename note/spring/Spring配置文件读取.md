## Spring Boot 配置文件加载顺序

Spring Boot 读取配置文件的顺序如下：

1. 命令行参数：可以通过命令行参数的方式指定配置文件的路径。例如，使用 `java -jar myproject.jar --spring.config.location=file:/etc/myproject/application.yml` 指定配置文件路径。
2. `SPRING_CONFIG_LOCATION` 环境变量：Spring Boot 会优先读取该环境变量所指定的配置文件路径。
3. 项目根目录下的 `config/` 目录：Spring Boot 会读取项目根目录下的 `config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
4. 项目根目录：Spring Boot 会读取项目根目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。
5. `classpath:/config/` 目录：Spring Boot 会读取 `classpath:/config/` 目录里的配置文件，包括 `application.properties`, `application.yml`, `application.yaml` 等文件。
6. `classpath:/` 目录：Spring Boot 会读取 `classpath:/` 目录下的 `application.properties`, `application.yml`, `application.yaml` 等文件。

注：优先级从高到低排列。如果同一配置文件存在于多个位置，则优先级高的位置会覆盖优先级低的位置的配置。同时，高优先级的位置中的配置文件也可以包含低优先级位置中的配置文件。例如，项目根目录下的 `application.properties`
可以使用 `@PropertySource("classpath:custom.properties")` 引入 `custom.properties` 文件中的配置。

`bootstrap.yml` 是 Spring Boot 应用程序的一个特殊的配置文件，用于在应用程序启动之前加载。它的作用是在 Spring 应用程序的上下文创建之前加载外部配置，通常用于一些框架的初始化和配置，例如连接配置中心、加密/解密属性等。
与 `application.yml` 或 `application.properties` 不同，`bootstrap.yml` 的加载顺序更早，因此可以用来配置一些在应用程序启动之前就需要用到的参数。
它的配置格式与 `application.yml` 或 `application.properties` 类似，但主要用于一些与应用程序启动相关的配置。
一般情况下，`bootstrap.yml` 用于配置一些基础设施相关的配置，例如配置中心、分布式追踪、加密/解密等。

当你使用配置中心时，配置中心的配置会在应用程序启动时被加载，并且它的加载优先级高于本地的配置文件。加载顺序如下：

1. **配置中心的配置被加载**：
    - 如果你启用了配置中心（例如使用 Consul、Spring Cloud Config 等），应用程序会首先尝试从配置中心获取配置。这些配置会在应用程序启动时被加载，并覆盖本地配置文件中相同键的配置。

2. **本地配置文件的配置被加载**：
    - 如果配置中心中不存在某些配置项，或者配置中心不可用，应用程序会回退到本地的配置文件中查找配置。本地配置文件中的配置会在配置中心加载之后被加载，但在没有配置中心的情况下，本地配置文件中的配置会在应用程序启动时被最先加载。

综上所述，配置中心的配置会在本地配置文件之前被加载，并覆盖本地配置文件中相同键的配置。这种加载顺序保证了配置中心的配置可以被应用程序灵活地管理和更新，同时也提供了一种在本地快速启动应用程序的方式。





