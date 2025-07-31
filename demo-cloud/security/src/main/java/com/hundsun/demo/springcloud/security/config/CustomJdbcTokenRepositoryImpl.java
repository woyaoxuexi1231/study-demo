package com.hundsun.demo.springcloud.security.config;

import com.hundsun.demo.springcloud.security.mapper.MyPersistentTokenRepository;
import lombok.Setter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Setter
public class CustomJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

    MyPersistentTokenRepository persistentTokenRepository;

    @Override
    public void removeUserTokens(String username) {
        // 覆盖这个方法，什么都不做，避免删除全部 token
    }

    public void removeToken(String series) {
        persistentTokenRepository.removeTokenBySeries(series);
    }
}
