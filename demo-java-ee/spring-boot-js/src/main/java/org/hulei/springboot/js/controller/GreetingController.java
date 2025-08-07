package org.hulei.springboot.js.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since  2024/3/10 13:48
 */

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting(@RequestParam(name = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok("Hello, " + name + "!");
    }
}
