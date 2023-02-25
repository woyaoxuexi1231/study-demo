package com.hundsun.demo.springboot.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.config
 * @className: DynamicDataSourceEnableConfig
 * @description:
 * @author: h1123
 * @createDate: 2023/2/25 19:13
 */

public class DynamicDataSourceEnableConfig implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return conditionContext.getEnvironment().getProperty("spring.datasource.dynamic.enable").equals("true");
    }
}
