package com.hundsun.demo.springcloud.security.remeberme;

import com.hundsun.demo.springcloud.security.mapper.MyPersistentTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * @author hulei
 * @since 2025/8/1 17:51
 */

@RequiredArgsConstructor
@Configuration
public class RememberMeConfig {

    private final DataSource dataSource;
    private final MyPersistentTokenRepository myPersistentTokenRepository;
    private final UserDetailsService userDetailsService;

    @Bean
    public RememberMeServices rememberMeServices() {
        return new CustomRememberMeServices(UUID.randomUUID().toString(), userDetailsService, tokenRepository());
    }

    @Bean
    public CustomJdbcTokenRepositoryImpl tokenRepository() {
        CustomJdbcTokenRepositoryImpl repo = new CustomJdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        repo.setPersistentTokenRepository(myPersistentTokenRepository);
        return repo;
    }

}
