package org.hulei.spring.reactive;

import lombok.RequiredArgsConstructor;
import org.hulei.entity.mybatisplus.domain.BigDataUsers;
import org.hulei.entity.mybatisplus.starter.mapper.BigDataUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author hulei
 * @since 2025/8/23 0:00
 */

@RequiredArgsConstructor
@RestController
public class UserController {

    final BigDataUserMapper bigDataUserMapper;

    @GetMapping("/get-user-by-id")
    public Mono<BigDataUsers> getUserById(@RequestParam("id") String id) {
        return Mono.justOrEmpty(bigDataUserMapper.selectById(id));
    }
}
