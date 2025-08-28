package com.hundsun.demo.springcloud.gateway.security;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @projectName: weibo
 * @package: org.weibo.hl.gateway.config
 * @className: IgnoreWhiteProperties
 * @description:
 * @author: hl
 * @createDate: 2023/5/21 17:11
 */

@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "ignore")
public class IgnoreWhiteProperties {

    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    @Getter
    private Set<String> whites = new HashSet<>();

    private String loginUrl;

    private String logoutUrl;

    public String getLoginUrl() {
        return StringUtils.isNotBlank(loginUrl) ? loginUrl : "/security/login";
    }

    public String getLogoutUrl() {
        return StringUtils.isNotBlank(logoutUrl) ? logoutUrl : "/security/logout";
    }

}
