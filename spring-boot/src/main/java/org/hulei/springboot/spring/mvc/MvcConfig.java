package org.hulei.springboot.spring.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: MvcConfig
 * @description:
 * @author: hulei42031
 * @createDate: 2023-04-02 13:20
 */

@Configuration
// @EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    MvcFilter mvcFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 这种方式不行,因为在这个阶段不一定能拿到,在内部注入的话,一定能拿到
        // MvcFilter filter = SpringbootApplication.applicationContext.getBean(MvcFilter.class);
        registry.addInterceptor(mvcFilter)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/login", "/register"); // 排除不需要拦截的路径
    }
}
