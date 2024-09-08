package org.hulei.jdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.jdk.annotation
 * @Description:
 * @Author: hulei42031
 * @Date: 2024-05-20 10:20
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2024 Hundsun Technologies Inc. All Rights Reserved
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SimpleAnnotation {
}
