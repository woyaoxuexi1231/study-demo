package org.hulei.springboot.spring.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hulei.springboot.SpringbootApplication;
import org.hulei.springboot.spring.listener.SimpleEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2024/12/27 11:45
 */

@EnableAsync(proxyTargetClass = true)
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/async")
@RestController
public class AsyncController {

    final AsyncService asyncService;

    @RequestMapping("/simple")
    public void simple() {
        asyncService.simple();
    }

    @RequestMapping("/push-event")
    public void pushEvent() {
        SpringbootApplication.applicationContext.publishEvent(new SimpleEvent(""));
    }

    @RequestMapping("/async-push-event")
    @Async("commonTaskExecutor")
    public void asyncPushEvent() {
        SpringbootApplication.applicationContext.publishEvent(new SimpleEvent(""));
    }

}
