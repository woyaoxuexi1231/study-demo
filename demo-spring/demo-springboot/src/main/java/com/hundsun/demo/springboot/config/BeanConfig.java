package com.hundsun.demo.springboot.config;

import com.hundsun.demo.commom.core.aop.DoneTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: BeanConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/12 2:12
 */
@Configuration
public class BeanConfig {

    @Bean
    public DoneTimeAspect doneTimeAspect() {
        return new DoneTimeAspect();
    }
}
