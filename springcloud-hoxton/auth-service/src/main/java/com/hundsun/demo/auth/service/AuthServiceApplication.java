package com.hundsun.demo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author woaixuexi
 * @since 2024/4/23 0:36
 */

@MapperScan(basePackages = {"com.hundsun.demo.auth.service.mapper"})
@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        /*
        获取 token
        curl -i -X POST -d "username=fzp&password=123456&grant_type=password&client_id=service-hi&client_secret=123456" http://localhost:5000/uaa/oauth/token
         */
        SpringApplication.run(AuthServiceApplication.class);
    }

    @Configuration
    @EnableAuthorizationServer
    protected class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

        private TokenStore tokenStore = new InMemoryTokenStore();

        //JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Qualifier("userDetailsService")
        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            clients.inMemory()
                    .withClient("browser")
                    .authorizedGrantTypes("refresh_token", "password")
                    .scopes("ui")
                    .and()
                    .withClient("service-hi")
                    .secret("123456")
                    .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                    .scopes("server");

        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore)
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients()
                    .passwordEncoder(NoOpPasswordEncoder.getInstance());

        }
    }
}
