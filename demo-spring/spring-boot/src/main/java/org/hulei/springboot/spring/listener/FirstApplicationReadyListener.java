package org.hulei.springboot.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2024/10/31 11:35
 */


// @Order(1)
@Slf4j
@Component
public class FirstApplicationReadyListener implements ApplicationListener<MyApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(MyApplicationReadyEvent event) {
        log.info("{}", event);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
