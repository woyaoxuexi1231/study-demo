package com.hundsun.demo.springcloud.security.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author hulei
 * @since 2025/7/25 22:06
 */

public interface MyPersistentTokenRepository extends PersistentTokenRepository {
    /**
     * 根据 series 删除单条令牌记录
     * @param series 令牌序列（唯一标识用户的令牌系列）
     */
    void removeTokenBySeries(String series) throws DataAccessException;
}
