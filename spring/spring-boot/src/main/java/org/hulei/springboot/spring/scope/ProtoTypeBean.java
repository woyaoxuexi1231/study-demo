package org.hulei.springboot.spring.scope;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
每次注入或通过 getBean 获取时创建新的实例。
测试会发现一个点是容器启动阶段所有的bean注入的也都是同一个bean
 */
@Scope(scopeName = "prototype")
@Component
public class ProtoTypeBean {
    final String name = "prototype";
}
