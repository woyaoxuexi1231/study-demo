package com.hundsun.demo.springcloud.security.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author hulei
 * @since 2025/8/1 21:00
 */

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        /*
        当 allowCredentials 设置为 true（允许跨域请求携带凭据如 cookies）时
        浏览器要求 Access-Control-Allow-Origin 响应头必须明确指定具体的域名（如 https://example.com），而不能使用通配符 "*"。

        为什么这样设计？
        这是浏览器的安全策略，防止恶意网站通过通配符窃取用户凭据。明确域名可以确保服务器只与信任的源共享敏感数据。
         */
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许跨域的站点
        corsConfiguration.setAllowedOrigins(List.of("*"));
        // 允许跨域的方法
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST"));
        // 允许带凭证
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
