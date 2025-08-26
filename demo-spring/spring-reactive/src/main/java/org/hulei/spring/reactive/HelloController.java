package org.hulei.spring.reactive;

import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author hulei
 * @since 2025/8/22 23:39
 */

@RestController
public class HelloController {

    @GetMapping("/")
    public Mono<String> hello() {
        return Mono.just("Hello World");
    }

}
