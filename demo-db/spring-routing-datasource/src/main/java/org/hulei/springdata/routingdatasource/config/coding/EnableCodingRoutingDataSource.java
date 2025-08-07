package org.hulei.springdata.routingdatasource.config.coding;

import org.hulei.springdata.routingdatasource.config.parsing.RoutingDataSourceBeanRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hulei
 * @since 2025/8/7 23:08
 */


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {RoutingCodingDataSourceAutoConfig.class})
public @interface EnableCodingRoutingDataSource {
}
