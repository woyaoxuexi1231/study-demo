package org.hulei.common.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/9 1:39
 */

@Slf4j
@RestController
public class UserCheckController {

    @GetMapping("/user-check")
    public void userCheck() {
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("当前用户为 ：{}，已登录", context.getAuthentication().getPrincipal());
    }
}
