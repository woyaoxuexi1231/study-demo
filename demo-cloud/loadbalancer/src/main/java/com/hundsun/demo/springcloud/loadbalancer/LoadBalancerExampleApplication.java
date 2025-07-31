package com.hundsun.demo.springcloud.loadbalancer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.hulei.util.dto.ResultDTO;
import org.hulei.util.dto.SimpleReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@EnableFeignClients
@SpringBootApplication
public class LoadBalancerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerExampleApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        /*
        通过 @LoadBalanced 注解启用负载均衡的 RestTemplate
        如果不开启 @LoadBalanced 注解，不会开启微服务的调用模式

        支持的负载均衡算法有：
            - RoundRobin	默认策略，轮询每个实例
            - Random	随机选择一个实例
            - WeightedResponseTime	基于实例的响应时间加权选择
            - 自定义策略	可以扩展接口实现你自己的策略

        此版本的 @LoadBalanced 会通过 LoadBalancerClient 寻找负载均衡策略，最终找到合适的服务
         */
        // return new RestTemplateBuilder().rootUri("http://eureka-client/").build();
        return new RestTemplate();
    }

    @RestController
    static class LoadBalancedController {

        String url = "http://netflix-provider/client/";
        // String url = "";

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Autowired
        LoadBalancedController loadBalancedController;

        @Autowired
        private RestTemplate restTemplate;

        @GetMapping("/hi")
        public String hi() {
            String url = "http://netflix-provider/client/";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                sb.append(restTemplate.getForObject(url + "hi", String.class)).append("\n");
            }
            // 无参格式
            return sb.toString();
        }

        @GetMapping("/choose")
        public String chooseServiceInstance() {
            // 使用LoadBalancerClient选择一个服务实例
            ServiceInstance serviceInstance = loadBalancerClient.choose("netflix-provider");
            return "Chosen service: " + serviceInstance.getHost() + ":" + serviceInstance.getPort();
        }

        @Autowired
        private DiscoveryClient discoveryClient;

        @GetMapping("/invoke-service")
        public String invokeService() {
            // 获取服务实例的信息
            List<ServiceInstance> instances = discoveryClient.getInstances("netflix-provider");
            if (instances.isEmpty()) {
                throw new RuntimeException("No instances available for eureka-client");
            }

            // 选择一个实例的地址进行调用（这里简单地选择第一个实例）
            ServiceInstance instance = instances.get(0);
            String url = instance.getUri() + "/hi";

            // 使用 RestTemplate 发起请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            return responseEntity.getBody();
        }

        @GetMapping(value = "circuitBreaker")
        public String circuitBreaker() {
            return loadBalancedController.callHelloService();
        }

        @CircuitBreaker(name = "helloService", fallbackMethod = "fallbackHello")
        public String callHelloService() {
            return Objects.requireNonNull(restTemplate.getForObject(url + "hi2?req={1}", ResultDTO.class, "hello")).toString();
        }

        public String fallbackHello(Throwable throwable) {
            return "Fallback: Unable to call hello service!";
        }
    }
}
