package org.hulei.springboot.spring.lazy;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author woaixuexi
 * @since 2024/3/25 0:31
 */

@Lazy
@Component
public class LazyBean {

    public LazyBean() {
        System.out.println("LazyBean此时开始实例化");
    }

    @PostConstruct
    public void init() {
        System.out.println("LazyBean此时开始初始化");
    }

    public void print() {
        System.out.println("this is lazy bean");
    }
}
