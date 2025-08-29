package org.hulei.demo.cj.ratelimit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hulei
 * @since 2025/8/28 15:17
 */

@Slf4j
@RequestMapping("/ratelimit")
@RestController
public class RatelimitTestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/test")
    public void test(@RequestParam("path") String path) {
        for (int i = 20; i > 0; i--) {
            log.info("{}", restTemplate.getForObject("http://localhost:19999" + path, String.class));
        }
    }
}
