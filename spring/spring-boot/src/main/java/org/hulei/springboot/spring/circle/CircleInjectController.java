package org.hulei.springboot.spring.circle;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/3/24 19:18
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/circle")
public class CircleInjectController {

    private final ClassA classA;
    private final ClassB classB;

    @GetMapping("/print")
    public void print() {
        classA.methodA();
        classB.methodB();
    }
}
