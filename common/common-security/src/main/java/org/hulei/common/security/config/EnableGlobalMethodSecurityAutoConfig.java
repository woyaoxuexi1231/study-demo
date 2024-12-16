package org.hulei.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author hulei
 * @since 2024/10/14 20:48
 */

/*
允许在方法级别进行安全验证。
1. prePostEnabled = true
    启用对 @PreAuthorize 和 @PostAuthorize 注解的支持，默认为 false
2. securedEnabled = true
    启用对 @Secured 注解的支持，默认为 false
3. jsr250Enabled = true
    启用对 JSR-250 注解（如@RolesAllowed）的支持，默认为false
 */
@ConditionalOnProperty(name = "common.security.global.method", havingValue = "true")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class EnableGlobalMethodSecurityAutoConfig {
}
