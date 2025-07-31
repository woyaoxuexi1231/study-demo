package org.hulei.springboot.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/10/31 11:39
 */

// @Order(2)
@Slf4j
@Component
public class SecondApplicationReadyListener implements ApplicationListener<MyApplicationReadyEvent>, Ordered {

    // @EventListener
    // public void onApplicationReady(MyApplicationReadyEvent event) {
    //     log.info("{}", event);
    // }

    @Override
    public void onApplicationEvent(MyApplicationReadyEvent event) {
        log.info("{}", event);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
