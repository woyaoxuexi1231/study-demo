package org.hulei.springcloud.client;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.server
 * @className: EurekaServerApplication
 * @description:
 * @author: h1123
 * @createDate: 2023/5/5 20:44
 */

/*
服务注册: 服务注册是指将服务的信息（如服务名称、主机名、IP 地址、端口号等）注册到服务注册中心中，以便其他服务能够发现并与之通信

服务续约: Eureka 服务续约是指在服务实例启动后，周期性地向 Eureka 服务器发送心跳以维持注册状态的过程。续约操作告知 Eureka 服务器该服务实例仍然处于运行状态，并更新了其健康状态信息。续约操作使得 Eureka 服务器能够动态地了解服务实例的状态，并相应地对服务实例的可用性进行监控和管理。
服务续约的周期由服务实例的配置决定，通常默认为 30 秒

获取服务注册信息: Eureka 的获取服务注册信息是指通过 Eureka 服务器查询已注册的服务信息，以便于服务间的通信。在一个微服务架构中，不同的服务需要相互发现并通信，因此需要一种机制来获取其他服务的注册信息。Eureka 提供了一种基于 REST API 的方式来获取服务注册信息，这样可以使得服务间的通信更加简便和可靠。
信息会缓存在本地,同样的30秒更新一次

服务下线:
 */

@Slf4j
@RestController
/*
@EnableEurekaClient 和 @EnableDiscoveryClient 是用于 Spring Cloud 的服务注册与发现的注解
- @EnableEurekaClient 专用于 Eureka 作为服务注册中心的场景。它依赖于 Spring Cloud Netflix。
- @EnableDiscoveryClient 这是一个更通用的注解，支持多个服务发现组件。使用这个注解可以在切换服务注册中心实现时提供更大的灵活性。
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientApplication implements ApplicationRunner {

    public static void main(String[] args) {

        /*
        DiscoveryClient_EUREKA-CLIENT/LAPTOP-HGITO649:eureka-client:9101 - registration status: 204
        注册成功
         */
        ConfigurableApplicationContext context = SpringApplication.run(EurekaClientApplication.class, args);
    }

    @PreDestroy
    public void destroy() {
        // 服务下线,已经不推荐使用了
        // DiscoveryManager.getInstance().shutdownComponent();
    }

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public ThreadPoolExecutor commonPool() {
        return new ThreadPoolExecutor(
                10,
                10,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNamePrefix("commonPool-").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @GetMapping("/test")
    public void test() {
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<Object> ipResponse = restTemplate.exchange("http://httpbin.org:80/ip", HttpMethod.GET, entity, Object.class);
        ResponseEntity<Object> jsonResponse = restTemplate.exchange("http://httpbin.org:80/json", HttpMethod.GET, entity, Object.class);

        System.out.println(ipResponse.getBody());
        System.out.println(jsonResponse.getBody());
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
