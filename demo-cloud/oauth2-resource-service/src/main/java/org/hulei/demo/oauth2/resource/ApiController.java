package org.hulei.demo.oauth2.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {

    @PreAuthorize("hasAuthority('read')")
    @GetMapping("/api/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> user = new HashMap<>();
        user.put("sub", jwt.getSubject());
        user.put("email", jwt.getClaimAsString("email"));
        user.put("name", jwt.getClaimAsString("name"));
        user.put("scopes", jwt.getClaimAsStringList("scope"));
        log.info("返回用户：{}", user);
        return user;
    }
}
