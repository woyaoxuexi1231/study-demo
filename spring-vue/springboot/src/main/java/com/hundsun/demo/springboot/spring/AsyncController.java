package com.hundsun.demo.springboot.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Autowired
    SubAsync subAsync;

    // 这里如果不使用懒加载，由于@Async的原因，会导致启动失败
    // Bean with name 'asyncController' has been injected into other beans [asyncController] in its raw version as part of a circular reference, but has eventually been wrapped.
    // This means that said other beans do not use the final version of the bean.
    // This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
    // 这是因为AsyncAnnotationBeanPostProcessor执行的时机不太秒
    @Lazy
    @Autowired
    AsyncController asyncController;

    @RequestMapping("/print")
    public void print() {
        log.info("this is print");
        /*
        这里因为@Async引申出的事务问题和@Transactional本身导致事务失效的原理其实差不多
        spring事务绑定在线程上，如果前后在同一线程，那么事务是可以被统一管理的，即不会出现事务失效
        所以以下三种情况，第一个事务不会失效，相当于内嵌了一个方法而已
        第二个和第三个都会失效，因为@Async起了作用，导致他们另一了一个线程执行逻辑，所以事务失效了
         */
        async();
        asyncController.async();
        subAsync.async();
    }

    @Async
    public void async() {
        log.info("this is async");
    }

}

@Component
@EnableAsync
@Slf4j
class SubAsync {

    @Async
    public void async() {
        log.info("this is async");
    }
}
