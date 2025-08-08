package org.hulei.springboot.spring.actuate;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hulei
 * @since 2025/8/8 15:50
 */

@Configuration
public class MyPrometheusConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        /*
        通过这个 bean 来提供一个 Prometheus 配置时自动的程序名字
         */
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
