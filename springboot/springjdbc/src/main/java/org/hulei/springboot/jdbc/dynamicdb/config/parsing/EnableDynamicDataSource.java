package org.hulei.springboot.jdbc.dynamicdb.config.parsing;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RoutingDataSourceBeanRegister.class})
public @interface EnableDynamicDataSource {
}