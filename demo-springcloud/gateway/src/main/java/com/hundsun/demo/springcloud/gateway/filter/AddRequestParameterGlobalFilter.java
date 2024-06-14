package com.hundsun.demo.springcloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 这个全局过滤器将会给所有的请求都加上一个请求参数
 *
 * @author woaixuexi
 * @since 2024/4/12 22:33
 */

@Component
@Slf4j
public class AddRequestParameterGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    @Autowired
    AddRequestParameterGatewayFilterFactory addRequestParameterGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 在这里添加参数
        log.info("{}", exchange.getRequest().getQueryParams());

        if (HttpMethod.GET.equals(exchange.getRequest().getMethod())) {

            AbstractNameValueGatewayFilterFactory.NameValueConfig config = new AbstractNameValueGatewayFilterFactory.NameValueConfig();
            config.setName("exampleParam");
            config.setValue("exampleValue");

            // 使用AddRequestParameterFilter添加请求参数
            GatewayFilter filter = addRequestParameterGatewayFilterFactory.apply(config);

            // 在调用过滤链之前执行自定义过滤器逻辑
            return filter.filter(exchange, chain);
        } else {

            // 对于非 GET 请求，继续执行过滤链
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
