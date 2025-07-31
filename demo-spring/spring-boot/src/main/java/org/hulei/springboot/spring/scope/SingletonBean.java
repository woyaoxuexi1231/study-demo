package org.hulei.springboot.spring.scope;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(scopeName = "singleton")
@Component
public class SingletonBean {
    final String name = "singleton";
}
