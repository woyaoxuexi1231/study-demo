package com.hundsun.demo.springcloud.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 一个过滤器工厂类,也是用于打印请求时长,顺便打印参数
 *
 * @author hl
 * @since 2023/5/25 0:36
 */

@Slf4j
// @Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> implements Ordered {

    /**
     * 用于在请求的属性中存储请求开始时间的键。
     */
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    /**
     * 用于指定配置类中参数的顺序。
     *
     * @return list
     */
    @Override
    public List<String> shortcutFieldOrder() {
        // 这个方法主要决定了配置类的参数顺序,然后决定注入到Config的哪个参数, application-requesttime-factory.yml 有配置
        return CollectionUtil.newArrayList("withParams", "prefix", "suffix");
        // return CollectionUtil.newArrayList("withParams", "suffix", "prefix");
    }

    public RequestTimeGatewayFilterFactory() {
        // 它的目的是初始化过滤器工厂并指定配置类的类型。
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                        if (startTime != null) {
                            StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms");
                            if (config.isWithParams()) {
                                sb.append(" params:").append(exchange.getRequest().getQueryParams());
                            }
                            log.info("prefix: {}, suffix: {}, withParams: {}", config.prefix, config.suffix, config.withParams);
                            log.info(sb.toString());
                        }
                    })
            );
        };
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    /**
     * 这个类的作用呢
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {

        private boolean withParams;

        private String prefix;

        private String suffix;

    }

/*
1. **GatewayFilter和AbstractGatewayFilterFactory的区别**

   - **GatewayFilter**: `GatewayFilter`是Spring Cloud Gateway中定义过滤器的核心接口，其实现类负责具体的过滤逻辑。当请求进入网关时，匹配到的路由会根据定义好的`GatewayFilter`链进行处理。这些处理可以包括修改请求头、查询参数、路径等操作。

   - **AbstractGatewayFilterFactory**: `AbstractGatewayFilterFactory`是一个抽象工厂类，用于简化`GatewayFilter`的实现和生成。它允许通过配置文件定义过滤器逻辑，使得不需要编写过滤器实现类的代码就能创建过滤器实例。这个工厂主要用于创建`GatewayFilter`实例，它提供了一种扩展方式，使得用户可以更灵活地配置过滤器中的逻辑。

2. **为什么需要AbstractGatewayFilterFactory**

   虽然`GatewayFilter`接口允许开发者实现自己的过滤器逻辑，但在实际开发中，直接实现`GatewayFilter`可能会导致代码的重复和不便于管理。比如，当你需要在多个地方使用相似的过滤器逻辑时，直接复制`GatewayFilter`实现可能不是最佳选择。

   `AbstractGatewayFilterFactory`提供了一种标准化的方法来通过配置而非代码来管理过滤器逻辑。通过继承`AbstractGatewayFilterFactory`，可以很方便地将过滤器参数化，并在配置文件中声明这些参数，从而减少了编码工作量，同时也使得过滤器逻辑更加灵活和易于配置。例如，可以通过简单的配置就能够实现对请求头的添加、修改或是对请求进行重定向等操作，而不需要修改代码。

总之，`GatewayFilter`是具体实现过滤逻辑的接口，而`AbstractGatewayFilterFactory`则是一个工厂类，用于基于配置生成`GatewayFilter`实例，两者共同提供了一种既灵活又强大的方式来控制和管理经过Spring Cloud Gateway的请求。
 */

/*
==============================================================================关于shortcutFieldOrder这个方法===============================================================================
指定参数顺序主要是为了正确、便捷地将外部配置映射到过滤器工厂的参数中。在Spring Cloud Gateway中，创建自定义过滤器工厂时，可以通过指定参数顺序让我们的自定义过滤器能够接收来自配置文件的参数。

这里是一个例子：

假设你正在创建一个自定义的Gateway过滤器，该过滤器需要接收两个参数：`prefix`和`suffix`。在自定义过滤器工厂类中，我们可能会有如下代码片段：

```java
public class AddPrefixAndSuffixGatewayFilterFactory
    extends AbstractGatewayFilterFactory<AddPrefixAndSuffixGatewayFilterFactory.Config> {

    public static class Config {
        // 在这里定义需要接收的配置属性
        private String prefix;
        private String suffix;

        // getters and setters ...
    }

    public AddPrefixAndSuffixGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // 这里实现过滤器的具体逻辑
        return (exchange, chain) -> {
            // 使用config中的prefix和suffix来处理请求/响应等
            // ...
            return chain.filter(exchange);
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // 这里我们指定了配置参数的顺序
        return Arrays.asList("prefix", "suffix");
    }
}
```

有了这段代码，你就可以在你的Spring Cloud Gateway路由配置中这样使用这个过滤器：

```yaml
spring:
  cloud:
    gateway:
      routes:
      - id: customFilter
        uri: http://example.com
        filters:
        - AddPrefixAndSuffix=myPrefix,mySuffix
```

在上述的YAML配置中，`AddPrefixAndSuffix=myPrefix,mySuffix`映射到了过滤器工厂的`prefix`和`suffix`属性。由于我们在`shortcutFieldOrder`方法中指定了参数的顺序是`prefix`后跟`suffix`，Spring Cloud Gateway知道`myPrefix`是第一个参数，应该映射到`prefix`属性，而`mySuffix`是第二个参数，映射到`suffix`属性。这样，你就不需要显式地指明每个属性的名字，只需要按顺序提供它们的值。

参数顺序的这种使用方式简化了配置文件的编写，并且使得它更加清晰可读。同时，在程序代码中指定顺序也减少了因参数名混淆或错误而引起的潜在配置问题。
 */
}
