// package org.hulei.demo.oauth2.server.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
// import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
// import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
// import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
// import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
// import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
// import org.springframework.security.oauth2.provider.token.TokenStore;
// import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
//
// /**
//  * @author hulei
//  * @since 2025/8/4 17:51
//  */
//
// @Configuration
// @EnableAuthorizationServer
// public class OauthAutoconfigureConfig extends AuthorizationServerConfigurerAdapter {
//
//     private TokenStore tokenStore = new InMemoryTokenStore();
//
//     // JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
//
//     @Autowired
//     private AuthenticationManager authenticationManager;
//
//     @Override
//     public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//         clients.inMemory()
//                 .withClient("browser")
//                 .authorizedGrantTypes("refresh_token", "password")
//                 .scopes("ui")
//                 .and()
//                 .withClient("service-hi")
//                 .secret("123456")
//                 .authorizedGrantTypes("client_credentials", "refresh_token", "password")
//                 .scopes("server");
//
//     }
//
//     @Override
//     public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//         endpoints
//                 .tokenStore(tokenStore)
//                 .authenticationManager(authenticationManager);
//     }
//
//     @Override
//     public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//         oauthServer
//                 .tokenKeyAccess("permitAll()")
//                 .checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients()
//                 .passwordEncoder(NoOpPasswordEncoder.getInstance());
//
//     }
//
//
// }
