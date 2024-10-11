package org.hulei.springdata.routingdatasource.config.parsing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author hulei
 * @since 2024/8/27 15:59
 */

@Slf4j
public class EnableParsingRoutingCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        boolean enable = "true".equals(conditionContext.getEnvironment().getProperty("spring.datasource.routing.parsing.enable"));
        log.info(enable ? "将启用动态多数据源配置" : "将不启动动态多数据源配置");
        return enable;
    }
}
