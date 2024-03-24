package com.hundsun.demo.springboot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woaixuexi
 * @since 2024/3/24 19:18
 */

@RestController
@RequestMapping("/circle")
public class CircleInjectController {

    @Autowired
    ClassA classA;

    @Autowired
    ClassB classB;

    @GetMapping("/print")
    public void print() {
        classA.methodA();
        classB.methodB();
    }
}
