package org.hulei.springboot.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/6/26 15:58
 */

@Component
@Slf4j
public class ContextRefreshedEventListenerDemo implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ContextRefreshedEvent ApplicationContext 初始化或刷新时（如调用 refresh() 方法）");
    }
}
