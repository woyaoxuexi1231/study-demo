package org.hulei.springboot.spring.aop.service.impl;

import org.hulei.common.autoconfigure.annotation.DoneTime;
import org.hulei.springboot.spring.aop.service.AopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hulei42031
 * @since 2024-03-13 19:13
 */

@Service
@Slf4j
public class AopServiceImpl implements AopService {

    @DoneTime
    @Override
    public void print() {
        log.info("AopServiceImpl print");
    }

    @DoneTime
    @Override
    public String hello() {
        return "hello aop";
    }
}
