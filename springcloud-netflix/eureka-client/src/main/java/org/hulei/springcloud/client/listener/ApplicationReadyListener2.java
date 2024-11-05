package org.hulei.springcloud.client.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/10/31 11:39
 */

@Component
public class ApplicationReadyListener2 {

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        System.out.println("Application ready");
    }

}
