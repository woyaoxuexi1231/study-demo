package org.hulei.springboot.spring.scope;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
prototype 只在你显式向容器索取时才生效（context.getBean()）。
也就是说每一个注入 prototype bean 的地方是不同 bean 对象，但是一旦注入，对象就被固定了。
 */
@Scope(scopeName = "prototype")
@Component
public class ProtoTypeBean {
    final String name = "prototype";
}
