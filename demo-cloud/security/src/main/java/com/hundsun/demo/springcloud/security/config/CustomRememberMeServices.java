package com.hundsun.demo.springcloud.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private final CustomJdbcTokenRepositoryImpl tokenRepository;

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService,
                                    CustomJdbcTokenRepositoryImpl tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String cookie = extractRememberMeCookie(request);
        if (cookie != null && cookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(cookie);
                String series = cookieTokens[0];
                tokenRepository.removeToken(series); // 只删当前 token
            } catch (Exception e) {
                // log it if necessary
            }
        }
        super.logout(request, response, authentication);
    }
}
