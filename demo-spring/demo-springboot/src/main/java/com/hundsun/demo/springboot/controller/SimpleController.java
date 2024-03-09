package com.hundsun.demo.springboot.controller;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.controller
 * @className: SimpleController
 * @description:
 * @author: h1123
 * @createDate: 2023/2/13 23:30
 */

@RequestMapping("/simple")
@RestController
@Slf4j
public class SimpleController implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
