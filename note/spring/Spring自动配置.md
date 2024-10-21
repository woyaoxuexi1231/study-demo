## spring.factories

文件被放在 **src/main/resources** 目录下的 **META-INF** 子目录中。

spring.factories 是仿照 Java 的 **SPI（Service Provider Interface）** 机制而出现的技术。
用于声明Spring自动配置类，便于Spring启动时扫描这个文件，根据声明自动装配响应的组件。

## additional-spring-configuration-metadata.json

用于 Spring Boot 应用程序的自动配置元数据文件。
这个元数据文件包含了应用程序中使用的配置属性的描述信息，例如属性名称、类型、默认值、描述等。
IDE 可以根据这些元数据来提供智能代码补全、属性验证和提示等功能。

