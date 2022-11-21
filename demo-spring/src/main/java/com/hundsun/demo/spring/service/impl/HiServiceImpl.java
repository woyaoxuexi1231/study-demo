package com.hundsun.demo.spring.service.impl;

import com.hundsun.demo.spring.model.IgnoreBean;
import com.hundsun.demo.spring.service.HiService;
import com.hundsun.demo.spring.service.IgnoreAware;
import com.hundsun.demo.spring.service.ResolvableDependencyA;
import com.hundsun.demo.spring.service.ResolvableDependencyB;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ProductName: Hundsun amust
 * @ProjectName: study-demo
 * @Package: com.hundsun.demo.spring.service.impl
 * @Description:
 * @Author: hulei42031
 * @Date: 2022-07-22 16:21
 * @UpdateRemark:
 * @Version: 1.0
 * <p>
 * Copyright  2022 Hundsun Technologies Inc. All Rights Reserved
 */

@Component
@Slf4j
@ToString
public class HiServiceImpl implements HiService, ApplicationContextAware, IgnoreAware {

    ApplicationContext applicationContext;

    IgnoreBean ignoreBean;

    ResolvableDependencyA resolvableDependencyA;

    ResolvableDependencyB resolvableDependencyB;

    @Override
    public void sayHi() {
        log.info("hi");
    }

    public void init() {
        log.info("HiServiceImpl initializing...");
    }

    public void destroy() {
        log.info("HiServiceImpl destroying...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setIgnoreBean(IgnoreBean ignoreBean) {
        this.ignoreBean = ignoreBean;
    }

    public IgnoreBean getIgnoreBean() {
        return ignoreBean;
    }

    public ResolvableDependencyA getResolvableDependencyA() {
        return resolvableDependencyA;
    }

    public void setResolvableDependencyA(ResolvableDependencyA resolvableDependencyA) {
        this.resolvableDependencyA = resolvableDependencyA;
    }

    public ResolvableDependencyB getResolvableDependencyB() {
        return resolvableDependencyB;
    }

    public void setResolvableDependencyB(ResolvableDependencyB resolvableDependencyB) {
        this.resolvableDependencyB = resolvableDependencyB;
    }
}
