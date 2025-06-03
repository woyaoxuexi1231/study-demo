package org.hulei.springboot.spring.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author hulei
 * @since 2025/1/15 9:42
 */

@Component
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String banner = "";
        log.info("{}", banner);
        // throw new RuntimeException("error");
    }
}
