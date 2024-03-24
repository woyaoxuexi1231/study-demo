package com.hundsun.demo.springboot.spring.lazy;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author woaixuexi
 * @since 2024/3/25 0:31
 */

@Lazy
@Component
public class LazyBean {
    public void print() {
        System.out.println("this is lazy bean");
    }
}
