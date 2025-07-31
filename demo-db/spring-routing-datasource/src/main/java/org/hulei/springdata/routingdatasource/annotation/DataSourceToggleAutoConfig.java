package org.hulei.springdata.routingdatasource.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hulei
 * @since 2024/10/10 22:09
 */

@Configuration
public class DataSourceToggleAutoConfig {

    @ConditionalOnMissingBean(value = DataSourceToggleAop.class)
    @Bean
    public DataSourceToggleAop dataSourceToggleAop() {
        return new DataSourceToggleAop();
    }
}
