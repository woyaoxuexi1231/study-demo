package com.hundsun.demo.springboot.config;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.springboot.config
 * @Description:
 * @Author: hulei42031
 * @Date: 2023-11-28 18:45
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright 2023 Hundsun Technologies Inc. All Rights Reserved
 */

public class ConfigProperiesNote {
    /*
    springBoot 读取配置文件时会把大驼峰转换为 小写+横线 的形式存储
    具体代码在 org.springframework.boot.context.properties.bind.JavaBeanBinder.BeanProperty 内
    this.name = DataObjectPropertyName.toDashedForm(name);
    this.declaringClassType = declaringClassType;
    主要是这个方法 DataObjectPropertyName.toDashedForm
     */
}
