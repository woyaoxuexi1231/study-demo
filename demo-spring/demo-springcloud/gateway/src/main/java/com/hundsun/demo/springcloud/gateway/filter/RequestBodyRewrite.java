package com.hundsun.demo.springcloud.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class RequestBodyRewrite implements RewriteFunction<String, String> {

    private final ObjectMapper objectMapper;

    public RequestBodyRewrite(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * The method of obtaining the user name according to the user ID can be implemented internally according to the actual situation, such as library lookup or cache, or remote call
     *
     * @param userId
     * @return
     */
    private String mockUserName(int userId) {
        return "user-" + userId;
    }

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        try {
            Map<String, Object> map = objectMapper.readValue(body, Map.class);

            // Get id
            int userId = (Integer) map.get("user-id");

            // Write the map after obtaining the nanme
            map.put("user-name", mockUserName(userId));

            // Add a key/value
            map.put("gateway-request-tag", userId + "-" + System.currentTimeMillis());

            return Mono.just(objectMapper.writeValueAsString(map));
        } catch (Exception ex) {
            log.error("1. json process fail", ex);
            // Handling of exception in json operation
            return Mono.error(new Exception("1. json process fail", ex));
        }
    }
}