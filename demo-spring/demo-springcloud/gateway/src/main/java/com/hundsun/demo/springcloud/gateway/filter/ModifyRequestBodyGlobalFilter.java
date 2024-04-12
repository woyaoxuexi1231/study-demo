package com.hundsun.demo.springcloud.gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ModifyRequestBodyGlobalFilter implements GlobalFilter, Ordered {

    private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilterFactory;

    private final ObjectMapper objectMapper;

    @Autowired
    public ModifyRequestBodyGlobalFilter(ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilterFactory) {
        this.modifyRequestBodyFilterFactory = modifyRequestBodyFilterFactory;
        this.objectMapper = new ObjectMapper(); // 使用Spring的ObjectMapper
    }

    @Override
    public int getOrder() {
        // 优先级可以根据需要进行调整
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ModifyRequestBodyGatewayFilterFactory.Config config = new ModifyRequestBodyGatewayFilterFactory.Config()
                // 设置接收和发送的内容类型为 JSON
                .setContentType("application/json")
                // 设置请求体中类的类型
                .setInClass(String.class)
                .setOutClass(String.class)
                // 定义修改请求体的逻辑
                .setRewriteFunction(String.class, String.class, (exchange1, originalRequestBody) -> {
                    // 对原始请求体进行修改操作，这里以简单替换为例
                    // String modifiedBody = originalRequestBody.replaceAll("oldValue", "newValue");
                    // return Mono.just(modifiedBody);

                    try {
                        // 解析原始请求体
                        JsonNode jsonNode = objectMapper.readTree(originalRequestBody);
                        // 假设我们正在处理的是一个对象，我们可以直接转换它
                        if (jsonNode instanceof ObjectNode) {
                            ObjectNode objectNode = (ObjectNode) jsonNode;
                            // 新增参数
                            objectNode.put("newParam", "newValue");
                            String modifiedBody = objectNode.toString();
                            return Mono.just(modifiedBody);
                        }
                        // 如果原始请求体不是一个对象，直接返回原始体或根据实际情况进行处理
                        return Mono.just(originalRequestBody);
                    } catch (Exception e) {
                        // 处理JSON解析或转换时发生的异常
                        return Mono.error(e);
                    }

                });

        // 创建过滤器并将配置应用到该过滤器
        return modifyRequestBodyFilterFactory.apply(config).filter(exchange, chain);
    }
}