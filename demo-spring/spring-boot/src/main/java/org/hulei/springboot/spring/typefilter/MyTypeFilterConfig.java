package org.hulei.springboot.spring.typefilter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author hulei
 * @since 2025/6/26 13:00
 */

@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.CUSTOM,
        classes = {CustomTypeFilter.class}
))
@Configuration
public class MyTypeFilterConfig {
}
