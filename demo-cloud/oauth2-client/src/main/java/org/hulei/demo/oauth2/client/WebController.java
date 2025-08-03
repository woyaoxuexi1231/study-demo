package org.hulei.demo.oauth2.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @Value("${server.port}")
    String port;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("name", principal != null ? principal.getFullName() : "Anonymous");
        return "home";
    }

    @RequestMapping("/hi")
    public String home() {
        return "hi :"+",i am from port:" +port;
    }

}
