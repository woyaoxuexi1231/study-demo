package com.hundsun.demo.springboot.config;

import com.hundsun.demo.commom.core.aop.DoneTimeAspect;
import com.hundsun.demo.springboot.servlet.SimpleHttpSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
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

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(SimpleHttpSessionListener simpleHttpSessionListener) {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(simpleHttpSessionListener);
        return servletListenerRegistrationBean;
    }
}
