package org.hulei.springboot.spring.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author hulei
 * @since 2024/12/27 11:53
 */

public class SimpleEvent extends ApplicationEvent {

    public SimpleEvent(Object source) {
        super(source);
    }
}
