package com.hundsun.demo.springboot.spring;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import com.hundsun.demo.springboot.common.mapper.EmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {

    @Resource
    EmployeeMapper employeeMapper;

    @Resource
    SubAsync subAsync;

    // 这里如果不使用懒加载，由于@Async的原因，会导致启动失败
    // Bean with name 'asyncController' has been injected into other beans [asyncController] in its raw version as part of a circular reference, but has eventually been wrapped.
    // This means that said other beans do not use the final version of the bean.
    // This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
    // 这是因为AsyncAnnotationBeanPostProcessor执行的时机不太秒
    @Lazy
    @Autowired
    AsyncController asyncController;

    @Transactional
    @RequestMapping("/print")
    public void print() {
        /*
        这里因为@Async引申出的事务问题和@Transactional本身导致事务失效的原理其实差不多
        spring事务绑定在线程上，如果前后在同一线程，那么事务是可以被统一管理的，即不会出现事务失效
        所以以下三种情况，第一个事务不会失效，相当于内嵌了一个方法而已
        第二个和第三个都会失效，因为@Async起了作用，导致他们另一了一个线程执行逻辑，所以事务失效了
         */
        employeeMapper.updateByPrimaryKeySelective(EmployeeDO.builder().employeeNumber(1002L).lastName("Async3").build());
        // async();
        asyncController.async();
        subAsync.async();
    }

    @Async
    public void async() {
        log.info("this is async, 当前线程为: {}", Thread.currentThread().getName());
    }

}

@Component
@EnableAsync
@Slf4j
class SubAsync {

    /**
     * 这是一个带有 @Async 注解的方法, 此开启线程的默认为SimpleAsyncTaskExecutor,单看类的介绍,此类将不使用线程池,来一个任务创建一个,性能损耗极大
     */
    @Async
    public void async() {
        log.info("this is async, 当前线程为: {}", Thread.currentThread().getName());
    }
}


class BaseAsyncConfigurer implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池数量，方法: 返回可用处理器的Java虚拟机的数量。
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 最大线程数量
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 5);
        // 线程池的队列容量
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors() * 2);
        // 线程名称的前缀
        executor.setThreadNamePrefix("this-excutor-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        // executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /*异步任务中异常处理*/
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> {
            // todo 异步方法异常处理
            System.out.println("class#method: " + method.getDeclaringClass().getName() + "#" + method.getName());
            System.out.println("type        : " + ex.getClass().getName());
            System.out.println("exception   : " + ex.getMessage());
        };
    }

}
