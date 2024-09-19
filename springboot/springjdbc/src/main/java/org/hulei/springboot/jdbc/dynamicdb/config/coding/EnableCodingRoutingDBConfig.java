package org.hulei.springboot.jdbc.dynamicdb.config.coding;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 这是一个动态数据源是否开启的配置类,读取spring.datasource.dynamic.enable这个来决定是否开启多数据源
 *
 * @author h1123
 * @since 2023/2/25 19:13
 */

@Slf4j
public class EnableCodingRoutingDBConfig implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        boolean enable = "true".equals(conditionContext.getEnvironment().getProperty("spring.datasource.routing.coding.enable"));
        log.info(enable ? "将启用硬编码式多数据源配置" : "将不启动硬编码式多数据源配置");
        return enable;
    }
}
