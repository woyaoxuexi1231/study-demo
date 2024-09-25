package org.hulei.keeping.server.spring;

import com.hundsun.demo.commom.core.model.EmployeeDO;
import lombok.extern.slf4j.Slf4j;
import org.hulei.keeping.server.spring.circle.ClassA;
import com.hundsun.demo.commom.core.mapper.EmployeeMapperPlus;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
public class MyAsyncController {

    @Resource
    EmployeeMapperPlus employeeMapperPlus;

    @Resource
    SubAsync subAsync;

    /**
     * 在使用了 @Lazy 注解的情况下, spring在 {@link org.springframework.beans.factory.support.DefaultListableBeanFactory#resolveDependency(org.springframework.beans.factory.config.DependencyDescriptor, java.lang.String, java.util.Set, org.springframework.beans.TypeConverter)}
     * 这个方法内会对把需要注入的这个类替换成一个代理对象, 具体生成代理对象的方法是 org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver#buildLazyResolutionProxy(org.springframework.beans.factory.config.DependencyDescriptor, java.lang.String)
     * MyAsyncController 一定会被延迟加载,直到检测到有地方显示的使用了他才会被创建出来
     *
     * 循环依赖的过程在 {@link ClassA#methodA()}这里有
     * <p>
     * 这里如果不使用懒加载，由于@Async的原因，会导致启动失败
     * Bean with name 'asyncController' has been injected into other beans [asyncController] in its raw version as part of a circular reference, but has eventually been wrapped.
     * This means that said other beans do not use the final version of the bean.
     * This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
     * 这是因为 AsyncAnnotationBeanPostProcessor 执行的时机不太秒, AsyncAnnotationBeanPostProcessor 是为 @Async生成代理对象的后置处理器
     * <p>
     * AbstractAdvisorAutoProxyCreator->AbstractAutoProxyCreator->getEarlyBeanReference()方法解决循环依赖
     * AbstractAdvisingBeanPostProcessor->却没有实现SmartInstantiationAwareBeanPostProcessor接口来重写getEarlyBeanReference(),所以 AsyncAnnotationBeanPostProcessor 处理不了循环依赖的问题, 解决方案只能是加上 @Lazy注解
     * <p>
     * 发生异常的过程就在于,在存在循环依赖的前提下,第一次创建MyAsyncController的时候,三级缓存内有一个对象工厂了
     * 当属性填充的时候,发现需要注入自己,那么就会去一级缓存找,但是却发现没有,那么就会一直找到三级缓存,三级缓存拿到之后,代理对象会放入二级缓存
     * 时间点再回到第一次创建MyAsyncController的属性填充完毕之后,继续进行初始化操作,但是使用的AbstractAdvisingBeanPostProcessor,这导致再次生成代理对象
     * 继而生成了两个代理对象,导致依赖混乱了 (即使除了只有 @Async 修饰的类,那么这样第一次就不会生成代理对象,但是提前暴露的确实目标对象本身,也就是MyAsyncController,这样也还是不行)
     * <p>
     * TODO 但是为什么AbstractAdvisingBeanPostProcessor不解决循环依赖呢? 这个网上找不到答案,看来需要自己从代码中寻找了
     * 两者的共同点: 都继承了ProxyProcessorSupport, 都实现了BeanPostProcessor
     * 1. AbstractAutoProxyCreator由于需要从容器中获取上下文中所有的 Advisor,所以额外实现了 BeanFactoryAware,用来获取上下文
     * 2. 时间上看, AbstractAdvisingBeanPostProcessor好像后写, 功能上说, AbstractAdvisingBeanPostProcessor仅仅只能使用一个 advisor
     * 问题好像就出在上下文这里,既然AbstractAdvisingBeanPostProcessor用不到上下文,那么也拿不到上下文的三级缓存,那么自然也就解决不了循环依赖的问题, 所以还是老老实实写个 @Lazy
     */
    @Lazy
    @Autowired
    MyAsyncController myAsyncController;

    @Transactional
    @RequestMapping("/print")
    public void print() {
        /*
        这里因为 @Async 引申出的事务问题和 @Transactional 本身导致事务失效的原理其实差不多
        spring事务绑定在线程上，如果前后在同一线程，那么事务是可以被统一管理的，即不会出现事务失效
        所以以下三种情况，第一个事务不会失效，相当于内嵌了一个方法而已
        第二个和第三个都会失效，因为 @Async 起了作用，导致他们另一了一个线程执行逻辑，所以事务失效了
         */
        // async();
        myAsyncController.async();
        subAsync.async();
    }

    @Async
    public void async() {
        log.info("this is async, 当前线程为: {}", Thread.currentThread().getName());
        employeeMapperPlus.updateById(EmployeeDO.builder().employeeNumber(1002L).lastName("Async3").build());
        throw new RuntimeException("error");
    }

}

@Component
@EnableAsync
@Slf4j
class SubAsync {

    /**
     * 这是一个带有 @Async 注解的方法, 此开启线程的默认为 SimpleAsyncTaskExecutor,单看类的介绍,此类将不使用线程池,来一个任务创建一个,性能损耗极大
     */
    @Async
    public void async() {
        log.info("this is async, 当前线程为: {}", Thread.currentThread().getName());
    }
}

/**
 * 使用 AsyncConfigurer 接口可以自定义 @Async 使用的线程池, 如果不手动设置, 默认使用 SimpleAsyncTaskExecutor
 *
 * @author hulei
 * @since 2024/8/28 9:35
 */
@Configuration
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
        executor.setThreadNamePrefix("custom-async-thread-pool-");
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
