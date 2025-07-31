package org.hulei.springboot.spring.aop;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hulei42031
 * @since 2024-03-13 18:00
 */

@Configuration
public class AopAutoProxyConfig {

    /*
    使用 spring 的情况下，两种方式配置可以让 aop 生效
    1. @EnableAspectJAutoProxy
    2. 配置 AnnotationAwareAspectJAutoProxyCreator 这个 bean 对象

    @Bean
    public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
        AnnotationAwareAspectJAutoProxyCreator creator = new AnnotationAwareAspectJAutoProxyCreator();
        creator.setProxyTargetClass(true); // 使用CGLIB代理
        return creator;
    }

    使用 springboot 就完全不需要配置了。
    AopAutoConfiguration 类已经自动配置好了，通过 spring 的自动配置完全自动化的配置完成
    通过 AopAutoConfiguration 的配置，spring 会在 AspectJAutoProxyRegistrar 注册好 AnnotationAwareAspectJAutoProxyCreator
     */
}
