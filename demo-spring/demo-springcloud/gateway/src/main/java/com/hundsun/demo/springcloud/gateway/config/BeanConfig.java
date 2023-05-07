package com.hundsun.demo.springcloud.gateway.config;

import com.hundsun.demo.springcloud.gateway.resolver.HostAddrKeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.gateway.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/5/7 18:35
 */

@Configuration
public class BeanConfig {

    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver(){
        return new HostAddrKeyResolver();
    }
}
