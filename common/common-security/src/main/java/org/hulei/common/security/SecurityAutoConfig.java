package org.hulei.common.security;

import lombok.extern.slf4j.Slf4j;
import org.hulei.common.security.controller.UserCheckController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

/**
 * @author hulei
 * @since 2024/10/14 15:46
 */

@Slf4j
@MapperScan(basePackages = {"org.hulei.common.security.mapper"})
public class SecurityAutoConfig {

    /*
    以这样的形式创建会导致 MemorySecurityAutoConfig 内部声明的 Bean 失效, 具体原因需要看源码
    不过大概的原因可能是因为这个配置类被认为是一个简单的Bean对象,不再对内部声明的Bean进行扫描
    @ConditionalOnProperty(name = "common.security.strategy", havingValue = "configureradapter")
    @Bean
    public MemorySecurityAutoConfig myWebSecurityConfigurerAdapter() {
        log.info("创建了 MyWebSecurityConfigurerAdapter ");
        return new MemorySecurityAutoConfig();
    }
     */

    @Bean
    public UserCheckController userCheckController(){
        return new UserCheckController();
    }

}
