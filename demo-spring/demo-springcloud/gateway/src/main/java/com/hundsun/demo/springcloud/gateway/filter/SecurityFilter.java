package com.hundsun.demo.springcloud.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.hundsun.demo.commom.core.utils.ResultDTOBuild;
import com.hundsun.demo.springcloud.gateway.config.IgnoreWhiteProperties;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
// @Component
public class SecurityFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    IgnoreWhiteProperties ignoreWhiteProperties;

    @Autowired
    ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory;

    @Autowired
    LogReqGatewayFilterFactory logReqGatewayFilterFactory;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String url = uri.getPath();
        String host = uri.getHost();


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
            // String userId = stringRedisTemplate.opsForValue().get("weibo::security::token::" + token);
            String userId = "1";
            if (userId != null) {
                // 续约 token
                stringRedisTemplate.opsForValue().set("weibo::security::token::" + token, userId, 10 * 60 * 1000, TimeUnit.MILLISECONDS);
                if (exchange.getRequest().getMethod() == HttpMethod.GET) {
                    // get请求 处理参数
                    // 将现在的request，添加当前身份信息
                    ServerHttpRequest.Builder builder = request.mutate();
                    return addParameterForGetMethod(exchange, chain, uri, builder);
                } else if (exchange.getRequest().getMethod() == HttpMethod.POST) {
                    //封装我们的request
                    return chain.filter(exchange);
                }
                // todo 这里想在这里塞入参数, 卡住了
                /*
                这篇文章解决了这个问题
                https://programming.vip/docs/the-spring-cloud-gateway-modifies-the-content-of-the-request-and-response-body.html
                */
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
            if (regexPieces.length == 1 && org.apache.commons.lang3.StringUtils.isBlank(regexPieces[0])) {
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

    /**
     * get请求，添加参数
     *
     * @param exchange
     * @param chain
     * @param uri
     * @param builder
     * @return
     */
    private Mono<Void> addParameterForGetMethod(ServerWebExchange exchange, GatewayFilterChain chain, URI uri, ServerHttpRequest.Builder builder) {
        StringBuilder query = new StringBuilder();

        String originalQuery = uri.getQuery();
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }

        query.append("req2").append("=").append(2);

        try {
            URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build().encode().toUri();
            ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();
            return chain.filter(exchange.mutate().request(request).build());
        } catch (Exception e) {
            log.error("Invalid URI query: " + query, e);
            // 当前过滤器filter执行结束
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
    }

    protected DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

}
