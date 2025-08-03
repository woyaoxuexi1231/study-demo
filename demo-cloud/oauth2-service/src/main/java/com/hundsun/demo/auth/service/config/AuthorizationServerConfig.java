package com.hundsun.demo.auth.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        // 创建一个 RegisteredClient 的构建器（Builder），并为其分配一个随机生成的 UUID 作为唯一标识。
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                // 设置客户端的 ID（client_id），用于 OAuth2 协议中的客户端认证。
                .clientId("oauth-client")
                // 设置客户端的密钥（client_secret），用于客户端认证。{noop} 是 Spring Security 的密码编码前缀，表示密钥以明文形式存储（不加密）。
                .clientSecret("{noop}secret")
                // 添加客户端支持的授权类型（这里是 授权码模式）。AUTHORIZATION_CODE 是 OAuth2 的授权码模式（最常用的模式）。客户端需要通过授权码换取访问令牌（Access Token）。
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // 添加客户端支持的 刷新令牌 授权类型。允许客户端使用刷新令牌（Refresh Token）获取新的访问令牌。用于访问令牌过期后重新获取令牌，无需用户重新授权。
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 设置客户端的重定向 URI（授权服务器回调地址）。授权服务器在用户授权后，会将授权码通过这个 URI 回调给客户端。必须与客户端实际配置的 URI 完全一致（包括端口和路径）。
                // 这里是 http://localhost:8082/login/oauth2/code/oauth-client（典型的 Spring Security OAuth2 默认回调地址）。
                .redirectUri("http://localhost:8082/login/oauth2/code/oauth-client")
                // 设置客户端申请的权限范围（Scope）。"read" 表示客户端请求读取资源的权限。可以添加多个 Scope（如 .scope("read").scope("write")）。
                .scope("read")
                .build();

        // 创建一个基于内存的 RegisteredClientRepository，并将之前定义的 RegisteredClient 存入其中。
        return new InMemoryRegisteredClientRepository(client);
    }
}
