package com.hundsun.demo.springcloud.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class LogRequestBodyGlobalFilter implements GlobalFilter {
    private final List<HttpMessageReader<?>> messageReaders = getMessageReaders();

    private List<HttpMessageReader<?>> getMessageReaders() {
        return HandlerStrategies.withDefaults().messageReaders();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
       // Only apply on POST requests
        if (HttpMethod.POST.equals(exchange.getRequest().getMethod())) {
            return logRequestBody(exchange, chain);
        } else {
            return chain.filter(exchange);
        }
    }

    private Mono<Void> logRequestBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    DataBufferUtils.retain(dataBuffer);

                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };

                    return ServerRequest
                            // must construct a new exchange instance, same as below
                            .create(exchange.mutate().request(mutatedRequest).build(), messageReaders)
                            .bodyToMono(String.class)
                            .flatMap(body -> {
                                // do what ever you want with this body string, I logged it.
                                log.info("Request body: {}", body);
                                // by putting reutrn statement in here, urge Java to execute the above statements
                                // put this final return statement outside then you'll find out that above statements inside here are not executed.
                                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                            });
                });
    }
}