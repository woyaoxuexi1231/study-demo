package org.hulei.springboot.spring.scope;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * @author hulei42031
 * @since 2024-06-04 17:25
 */

@RequestMapping("/springBean")
@RestController
@Slf4j
@Component
public class SpringBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    public SpringBean() {
        /*
        org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean
        创建 bean 的第一步，createBeanInstance(beanName, mbd, args)
        通过调用 bean 的构造函数创建 bean，对 bean 进行实例化
         */
        log.info("1. 这是一个spring bean的生命周期的第一步: 实例化 createBeanInstance");
    }

    SingletonBean singletonBean;

    @Autowired
    public void setSimpleSpringBean(SingletonBean singletonBean) {
        /*
        在 bean 实例化后，对 bean 进行属性填充的阶段 populateBean(beanName, mbd, instanceWrapper)
        在此之前还有一个非常重要的步骤，就是提前暴露加入三级缓存，三级缓存会根据需要决定是否执行
         */
        log.info("2. 第二步: 属性填充 populateBean");
        this.singletonBean = singletonBean;
    }

    @Override
    public void setBeanName(String name) {
        /*
        Initialize the given bean instance, applying factory callbacks as well as init methods and bean post processors.
        Called from createBean for traditionally defined beans, and from initializeBean for existing bean instances.

        属性填充(populateBean)后紧接着进行的 initializeBean 阶段时触发的 Aware
        这个方法是由实现 BeanNameAware 得来，由希望在 Bean 工厂中了解其 Bean 名称的 Bean 实现的接口，一般都不使用这个方法。
         */
        log.info("3. 在bean实例化完成之后, 依赖注入完成之后, 会调用 setBeanName {}, 这个方法的作用仅仅是可以知道一个bean在factory内他的真实名字", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        /*
        属性填充(populateBean)后紧接着进行的 initializeBean 阶段时触发的 Aware，用于直到当前 bean 的上下文。
        允许 Bean 在生命周期中感知容器的基础设施（如名称、工厂），而无需依赖具体容器实现。
        Spring 自身的一些组件（如 ApplicationListenerDetector）依赖这些接口。

        和 BeanNameAware 除非需要扩展框架或访问底层容器，否则应优先使用标准的依赖注入。而且不常用。
         */
        log.info("4. setBeanFactory 可以知道这个bean对应的bean工厂是什么, 可以在这个阶段获取相关的bean, 但是一般DI的形式通过其他方式进行");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        /*
        实现 ApplicationContextAware 接口时，在 applyBeanPostProcessorsBeforeInitialization 阶段会调用这个方法

        ApplicationContextAware 的目的是：
            - 让 bean 可以获取容器引用（ApplicationContext 对象）
            - 进而可以做一些高级用法，比如：
                1. 通过容器动态获取其他 bean
                2. 注册事件
                3. 动态发布或管理 bean
                4. 作为桥梁，让一些普通 bean 能“感知” Spring 容器
        常见的场景：
            - 自定义框架里需要拿到 ApplicationContext 做注册或动态管理
            - 需要写工具类把 ApplicationContext 存到静态变量里，方便在非 Spring 管理的地方也能获取 bean

        applyBeanPostProcessorsBeforeInitialization 执行所有注册的 BeanPostProcessor 的 postProcessBeforeInitialization 方法 ：
            在 bean 初始化前做自定义处理，比如：
                - 给 bean 设置额外的依赖（自动代理、自动注入）
                - 检查、修改 bean 的属性
                - 做 Aware 接口的处理（就是在这里触发 ApplicationContextAwareProcessor）
                - Spring 的 AOP、事务、注解处理器（比如 @Autowired、@Resource）都会用到这套流程。
         */
        log.info("5. setApplicationContext, 一般通过这个方法获取上下文, 需要对应实现 ApplicationContextAware 这个接口");
    }

    @PostConstruct
    private void init() {
        /*
        同样的，实现了 @PostConstruct 方法会通过 InitDestroyAnnotationBeanPostProcessor 这个后置处理器来执行
        执行的时机同样在 applyBeanPostProcessorsBeforeInitialization 内，也仅仅比 ApplicationContextAware 稍慢一点(同一个循环内)

        这个阶段的特点是：
            1. Bean 已经被创建出来了
            2. 依赖也都注入好了（@Autowired、@Value 都已生效）
            3. 但是还没放到容器里“可用”
        所以非常适合做一些：
            - 依赖检查：确保注入的依赖不为空，或者做一些一致性校验。
            - 初始化逻辑：比如根据配置文件生成缓存、打开连接、启动线程池。
            - 根据注入的值进行计算：有时需要组合依赖生成新的属性值。
            - 注册一些监听器：如果是消息系统或事件驱动系统。
         */
        log.info("6. @PostConstruct, 这个方法将在实例化和依赖注入完成之后调用, 和InitializingBean接口实现的功能类似,不过这个方法比InitializingBean接口更先调用");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
        和 javax.annotation.PostConstruct 类似
        但是 spring 框架为了此方法单独抽出了一个流程，通过 invokeInitMethods(beanName, wrappedBean, mbd) 执行，所以这也意味着这个方法一定后执行
        如果只是做简单初始化，用 @PostConstruct 更清晰、更解耦。
        如果写框架、做抽象、或者需要覆盖父类的 afterPropertiesSet，可以用 InitializingBean。

        同样的，对于 @PreDestroy 和 DisposableBean 也是类似的道理
         */
        log.info("7. afterPropertiesSet, 这个方法将在实例化和依赖注入完成之后调用, 在 initializeBean(beanName, exposedObject, mbd) 这个方法内执行, 在这个方法内, 执行此方法前会执行 setBeanName 和 setBeanFactory 方法");
    }

    @PreDestroy
    private void preDestroy() {
        log.info("8. @PreDestroy, 容器摧毁时调用的方法, 此方法先于 DisposableBean 接口");
    }

    @Override
    public void destroy() throws Exception {
        // 相当于 javax.annotation.PreDestroy
        log.info("9. destroy, 相当于 @PreDestroy 注解");
    }

    @Autowired
    ProtoTypeBean protoTypeBean;

    @GetMapping("/protoTypeBeanTest")
    public void protoTypeBeanTest() {
        // for (int i = 0; i < 10; i++) {
        //     ProtoTypeBean bean = SpringbootApplication.applicationContext.getBean(ProtoTypeBean.class);
        //     log.info("获取的多例bean为: {}", bean);
        // }
        log.info("获取的多例bean为: {}", protoTypeBean);
    }

    @Autowired
    RequestBean requestBean;

    @GetMapping("/requestBeanTest")
    public void requestBeanTest() {
        log.info("获取的 request bean 为: {}", requestBean);
    }
}

