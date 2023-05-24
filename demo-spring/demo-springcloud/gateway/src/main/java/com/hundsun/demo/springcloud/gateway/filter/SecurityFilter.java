package com.hundsun.demo.springcloud.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.springcloud.gateway.config.IgnoreWhiteProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @projectName: weibo
 * @package: org.weibo.hl.gateway.filter
 * @className: SecurityFilter
 * @description:
 * @author: hl
 * @createDate: 2023/5/21 13:53
 */

@Slf4j
@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    IgnoreWhiteProperties ignoreWhiteProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 登录请求不验证 token
        if (ignoreWhiteProperties.getLoginUrl().equals(exchange.getRequest().getPath().value())) {
            return chain.filter(exchange);
        } else if (ignoreWhiteProperties.getLogoutUrl().equals(exchange.getRequest().getPath().value())) {
            // 清除 token
            HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("token");
            String token = tokenCookie == null ? " " : tokenCookie.getValue();
            stringRedisTemplate.delete("weibo::security::token::" + token);
            return chain.filter(exchange);
        } else {
            HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("token");
            String token = tokenCookie == null ? " " : tokenCookie.getValue();
            String userId = stringRedisTemplate.opsForValue().get("weibo::security::token::" + token);
            if (userId != null) {
                // 续约 token
                stringRedisTemplate.opsForValue().set("weibo::security::token::" + token, userId, 10 * 60 * 1000, TimeUnit.MILLISECONDS);

                // todo 这里想在这里塞入参数, 卡住了
                return chain.filter(exchange);
            } else {
                // 白名单
                if (isWhiteList(exchange.getRequest().getPath().value())) {
                    return chain.filter(exchange);
                } else {
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);

                    log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

                    return response.writeWith(Mono.fromSupplier(() -> {
                        DataBufferFactory bufferFactory = response.bufferFactory();
                        return bufferFactory.wrap(JSON.toJSONBytes(ResultDTOBuild.resultErrorBuild(401, "请登录后再进行操作! ")));
                    }));
                }
            }
        }
    }

    public boolean isWhiteList(String targetUrl) {

        // 分割 url
        for (String whiteUrl : ignoreWhiteProperties.getWhites()) {

            if (whiteUrl.startsWith("/")) {
                whiteUrl = whiteUrl.substring(1);
            }
            String[] regexPieces = whiteUrl.split("/");
            StringBuilder regex = new StringBuilder();
            if (regexPieces.length == 1 && StringUtils.isBlank(regexPieces[0])) {
                regex.append(".*");
            } else {
                for (String regexPiece : regexPieces) {
                    if (regexPiece.equals("*")) {
                        regex.append("/.*");
                    } else {
                        regex.append("/").append(regexPiece);
                    }
                }
            }
            if (Pattern.matches(regex.toString(), targetUrl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -100;
    }

    public static void main(String[] args) {

        System.out.println(Pattern.matches("/server/.*/123", "/server/getSearchResult/123"));
    }
}
