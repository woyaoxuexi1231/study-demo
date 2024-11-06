package org.hulei.springcloud.client.listener;

import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @author hulei
 * @since 2024/10/31 11:35
 */

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(event.getSource());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
