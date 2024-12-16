package com.hundsun.demo.springcloud.gateway.globalfilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 全局过滤器
 * <p>
 * {@link org.springframework.cloud.gateway.filter.GlobalFilter}
 * 不需要在配置文件中配置，作用在所有路由上，最终通过Gateway FilterAdapter包装成GatewayFilterChain可识别的过滤器。它是将请求业务以及路由的Url转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。
 * <p>
 * {@link org.springframework.cloud.gateway.filter.GatewayFilter}
 * 不需要在配置文件中配置，作用在所有路由上，最终通过Gateway FilterAdapter包装成GatewayFilterChain可识别的过滤器。它是将请求业务以及路由的Url转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。
 *
 * @author hl
 * @since 2023/5/25 0:37
 */

@Component
@Slf4j
public class TokenGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /*
        目前来看,请求一个不存在的接口也不会通过这个全局的过滤器,但是具体原因不太清楚
        猜测是因为gateway内部有异常处理机制,在请求还没到这个过滤器的时候就已经被返回了(我尝试把order设置为最高优先级也不行) 2024年4月12日
         */
        // String token = exchange.getRequest().getQueryParams().getFirst("token");
        // if (token == null || token.isEmpty()) {
        //     log.info("token is empty...");
        //     exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        //     return exchange.getResponse().setComplete();
        // }
        // log.info("token is {}", token);
        // return chain.filter(exchange);
        log.info("path: {}",exchange.getRequest().getPath().pathWithinApplication().value());
        log.info("path: {}",exchange.getRequest().getPath().contextPath().value());

        // 从请求的cookie中获取token
        Optional<String> token = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("token"))
                .map(HttpCookie::getValue);
        log.info("cookies is {}", exchange.getRequest().getCookies());

        if (!token.isPresent() || StringUtils.isBlank(token.get())) {
            log.info("token is empty...");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return -100;
    }
}

/*
在Spring Cloud Gateway中，通常情况下，全局过滤器应该对所有进入网关的请求生效，包括那些无法被路由到下游服务的请求。但是，根据你遇到的问题，即全局过滤器没有对一个不存在的接口请求生效，可能存在以下几种原因：

1. **过滤器未被注册**：确保你的过滤器类是在Spring应用的上下文中正确注册的。如果使用组件扫描，请确保你的过滤器所在的包被包括在扫描路径内。

2. **请求先被其他组件处理**：如果你的网关前还有其他组件，例如负载均衡器、防火墙等，在请求到达Spring Cloud Gateway之前，这些组件可能已经处理了请求并返回了404，因此网关连同全局过滤器都没有机会处理这个请求。

3. **内部路由逻辑处理**：Spring Cloud Gateway内部有自己的异常处理和路由逻辑，如果一个请求不匹配任何路由规则，它可能会在全局过滤器之前就被某种异常处理机制处理。

4. **路由定位的优先级**：在某些特定的配置之下，网关可能会首先尝试进行路由定位。如果没有找到任何匹配的路由，这个请求就会被快速失败，而不是继续传递到全局过滤器链中。这会导致全局过滤器没有机会执行。

如果你的情况符合上述任何一点，这可能解释了为什么全局过滤器没有被执行。为了解决这个问题，你可以尝试以下步骤来调试：

- **确认注册**：检查你的过滤器是否被Spring上下文正确注册。
- **查看日志**：增加日志输出，查看是否过滤器根本没有被加载或者是在处理过程中出现了错误。
- **异常处理**：在网关中添加自定义异常处理器，它能够捕获所有错误，包括未找到路由的404错误，并可以在这里执行特定的逻辑。

如果问题依旧存在，你可能需要进一步检查你的Spring Cloud Gateway配置和其他可能影响请求处理的中间件或组件。
 */
