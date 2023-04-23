package com.hundsun.demo.springboot.config;

import com.hundsun.demo.commom.core.aop.DoneTimeAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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

    // @Bean
    // public ServletListenerRegistrationBean servletListenerRegistrationBean(SimpleHttpSessionListener simpleHttpSessionListener) {
    //     ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
    //     servletListenerRegistrationBean.setListener(simpleHttpSessionListener);
    //     return servletListenerRegistrationBean;
    // }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
