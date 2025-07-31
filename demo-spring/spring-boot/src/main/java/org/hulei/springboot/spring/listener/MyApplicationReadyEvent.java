package org.hulei.springboot.spring.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author hulei
 * @since 2024/10/31 11:37
 */

public class MyApplicationReadyEvent extends ApplicationEvent {

    public MyApplicationReadyEvent(Object source) {
        super(source);
    }
}
