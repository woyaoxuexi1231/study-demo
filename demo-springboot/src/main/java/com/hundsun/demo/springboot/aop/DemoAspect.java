package com.hundsun.demo.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springboot.aop
 * @className: DemoAspect
 * @description:
 * @author: h1123
 * @createDate: 2022/11/1 19:53
 * @updateUser: h1123
 * @updateDate: 2022/11/1 19:53
 * @updateRemark:
 * @version: v1.0
 * @see :
 */
@Aspect
@Component
@Slf4j
public class DemoAspect {

    /**
     * todo
     * 这里有个疑问就是, 表达式里面为什么可以写接口, 然而如果真的是一个纯接口, 又不能是spring的一个bean了
     * 但是单单作为cglib来说, cglib非常强大, cglib可以直接生成一个纯接口的代理对象, 这个代理对象会实现这个需要代理的接口不继承任何类
     * (cglib代理类的话会继承这个类然后实现 net.sf.cglib.proxy.Factory)
     */
    @Pointcut(value = "execution(public * com.hundsun.demo.springboot.service.serviceimpl.*.*(..))")
    public void jointpoint1() {

    }

    @Before(value = "jointpoint1()")
    public void around(JoinPoint joinPoint) {
        try {
            log.info("demo-aspect...");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
