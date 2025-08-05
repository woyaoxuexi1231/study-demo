// package org.hulei.demo.oauth2.server.sb2;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.crypto.factory.PasswordEncoderFactories;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
// import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
// import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
// import org.springframework.security.oauth2.provider.token.TokenStore;
// import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
// import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
// import java.security.KeyPair;
//
// @EnableAuthorizationServer
// @Configuration
// public class JwkSetConfiguration extends AuthorizationServerConfigurerAdapter {
//
//
//     private AuthenticationManager authenticationManager;
//
//     private KeyPair keyPair;
//
//     // ... client configuration, etc.
//
// 	public JwkSetConfiguration(AuthenticationManager authenticationManager, KeyPair keyPair) {
// 		this.authenticationManager = authenticationManager;
// 		this.keyPair = keyPair;
// 	}
//
// 	@Override
//     public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
//         // @formatter:off
// 		endpoints
// 			.authenticationManager(this.authenticationManager)
// 			.accessTokenConverter(accessTokenConverter())
// 			.tokenStore(tokenStore());
// 		// @formatter:on
//     }
//
//     @Bean
//     public TokenStore tokenStore() {
//         return new JwtTokenStore(accessTokenConverter());
//     }
//
//     @Bean
//     public JwtAccessTokenConverter accessTokenConverter() {
//         JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//         converter.setKeyPair(this.keyPair);
//         return converter;
//     }
//
//
//     @Bean
//     PasswordEncoder passwordEncoder() {
//         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//     }
//
//     public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//         clients
//                 /*
//                 对于第一部分：
//                 1. 获取授权码 http://localhost:9000/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=first-client&state=1234
//                 2. 如果用户未登录，则应用程序将重定向到登录页面，地址为 http://localhost:8080/login
//                 3. 用户登录后，应用程序会生成一个代码并重定向到注册的重定向 URI（在本例中为 http://localhost:8081/oauth/login/client-app
//                  */
//                 .inMemory()
//                 .withClient("first-client")
//                 .secret(passwordEncoder().encode("noonewilleverguess"))
//                 .scopes("resource:read")
//                 .authorizedGrantTypes("authorization_code")
//                 .redirectUris("http://localhost:8081/oauth/login/client-app")
//
//
//                 .and()
//                 // 配置客户端的id，这个 id 和 github 上申请服务id时申请的id是一个意思
//                 .withClient("dcb722a8-bc12-4fbc-82f5-a86cfa1be57c")
//                 // 相当于这是发放给客户端的密钥，主要用于 客户端身份验证，保护敏感流程
//                 .secret("{noop}6f7b7f80-ff7d-487f-aa73-57d1621a4d6d")
//                 // 可以指定该客户端能申请的OAuth2 Scope列表（如read, write, user_info等）。客户端在申请令牌时，只能使用这些预定义的Scope，超出范围会被拒绝。
//                 // Scope是OAuth2的权限标识符，但实际权限验证需结合资源服务器的@PreAuthorize("#oauth2.hasScope('read')")等注解实现。
//                 .scopes("read", "write")
//                 /*
//                 authorizedGrantTypes 指定该客户端可以使用哪些模式
//                   - authorization_code（授权码模式，最安全，适用于 Web 后端应用）
//                   - password（密码模式，已废弃，不推荐使用）
//                   - client_credentials（客户端凭证模式，适用于服务端间通信）
//                   - implicit（隐式模式，已废弃，用于旧版 SPA）
//                   - refresh_token（允许使用刷新令牌）
//                  */
//                 .authorizedGrantTypes("authorization_code", "refresh_token")
//                 /*
//                 OAuth2 要求客户端必须预先在授权服务器注册合法的 redirect_uri。
//                 当用户授权完成后，授权服务器 只会 将授权码（或令牌）发送到预先注册的 URI，避免恶意劫持。
//                 支持动态回调（可选）
//                 授权码模式（authorization_code）：必须配置 redirectUris，因为授权码需要通过回调 URI 返回客户端。
//                  */
//                 .redirectUris("http://localhost:8082/login/oauth2/code/oauth2-server")
//         ;
//     }
// }