package org.hulei.springcloud.client.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @author hulei
 * @since 2024/10/31 11:37
 */

public class ApplicationReadyEvent extends ApplicationEvent {

    public ApplicationReadyEvent(Object source) {
        super(source);
    }
}
