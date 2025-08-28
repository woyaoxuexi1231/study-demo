package com.hundsun.demo.springcloud.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
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
        log.info("path: {}",exchange.getRequest().getPath().pathWithinApplication().value());
        log.info("path: {}",exchange.getRequest().getPath().contextPath().value());

        // 从请求的cookie中获取token
        Optional<String> token = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("token"))
                .map(HttpCookie::getValue);
        log.info("cookies is {}", exchange.getRequest().getCookies());

        if (token.isEmpty() || StringUtils.isBlank(token.get())) {
            log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
            // 构造错误响应体（JSON 格式）
            String errorJson = "{\"code\":401,\"message\":\"Unauthorized: Token is missing or empty\"}";
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 将错误信息写入响应体
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(errorJson.getBytes(StandardCharsets.UTF_8)))
            );
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return -100;
    }
}