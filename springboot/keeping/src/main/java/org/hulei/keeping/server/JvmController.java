package org.hulei.keeping.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hulei
 * @since 2024/9/15 23:37
 */

@RestController("/jvm")
public class JvmController {

    List<Object> list = new ArrayList<>();

    @GetMapping("/generateObjects")
    public void generateObjects() {
        for (int i = 0; i < 1000; i++) {
            list.add(new Object());
        }
    }

    @GetMapping("/clearObjects")
    public void clearObjects() {
        list.clear();
    }
}
