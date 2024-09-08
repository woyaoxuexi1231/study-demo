package com.hundsun.demo.springboot.js.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hulei
 * @since 2024/8/19 9:49
 */

@RestController
@RequestMapping("/cors")
public class CorsController {

    @CrossOrigin
    @GetMapping("/getCorsString")
    public String getCorsString() {
        return "hello cors!";
    }
}


// @Configuration
class MyCorsConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 放行哪些原始域
        config.addAllowedOrigin("*");
        // 是否发送 Cookie
        config.setAllowCredentials(true);
        // 放行哪些请求方式
        config.addAllowedMethod("*");
        // 放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        // 暴露哪些头部信息
        config.addExposedHeader("*");
        // 2. 添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 3. 返回新的CorsFilter
        return new CorsFilter(source);
    }
}

// @Configuration
class MyCorsMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .maxAge(3600);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
