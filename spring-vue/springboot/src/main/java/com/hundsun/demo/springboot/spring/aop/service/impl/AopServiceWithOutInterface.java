package com.hundsun.demo.springboot.spring.aop.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 一个没有实现任何接口的类, 在spring的代理模式中, 他会用cglib来生成代理对象
 *
 * @author hulei42031
 * @since 2023-09-15 13:24
 */

@Slf4j
@Service
public class AopServiceWithOutInterface {

    public void print() {
        log.info("{}", "print");
    }
}
