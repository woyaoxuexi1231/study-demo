package org.hulei.mybatis.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hulei
 * @since 2024/11/17 16:19
 */

@MapperScan(basePackages = {"org.hulei.mybatis.mapper"})
@SpringBootApplication
public class MybatisApplication {

    public static void main(String[] args) {
        /*
        MyBatis 中的设计模式
        1. 建造者模式
            SqlSessionFactoryBuilder
            复杂配置对象的构建过程分离，支持链式调用
        2. 工厂模式
            SqlSessionFactory (接口) + DefaultSqlSessionFactory (实现)
            统一管理 SqlSession 等核心对象的创建
        3. 代理模式 (Proxy Pattern)
            Mapper 接口的 JDK 动态代理 (MapperProxy)
            延迟加载的代理实现 (JavassistProxyFactory, CglibProxyFactory)
        4. 责任链模式 (Chain of Responsibility)
            InterceptorChain
            Executor 的插件体系
            作用：支持用户自定义插件对执行过程进行拦截
        5. 模板方法模式 (Template Method)
            BaseExecutor 及其子类 (SimpleExecutor, BatchExecutor, ReuseExecutor)
            BaseTypeHandler
            作用：定义SQL执行骨架，子类实现特定步骤
        6. 装饰器模式 (Decorator Pattern)
            CachingExecutor (对基本 Executor 的缓存装饰)
            各种 Wrapper 类
            作用：动态添加缓存等能力而不修改原有结构
        7. 单例模式 (Singleton Pattern)
        8. 策略模式 (Strategy Pattern)
            应用场景：算法选择
            Executor 的不同实现类
            ParameterHandler/ResultSetHandler 的不同处理策略
            作用：根据场景动态切换SQL执行策略
        9. 组合模式 (Composite Pattern)
            应用场景：SQL节点处理
            SqlNode 及其实现类 (IfSqlNode, MixedSqlNode, TextSqlNode)
            作用：构建动态SQL的树形结构
        10. 适配器模式 (Adapter Pattern)
            应用场景：日志模块集成
            各种日志框架的适配器 (Log4jImpl, Slf4jImpl, Jdk14LoggingImpl)
         */
        SpringApplication.run(MybatisApplication.class, args);
    }

    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                5,
                5,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadFactory() {

                    final AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("mybatisPlusThread-" + atomicInteger.getAndIncrement());
                        return thread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
