package com.hundsun.demo.springcloud.loadbalancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LoadBalancerExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerExampleApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    static class LoadBalancedController {

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Autowired
        private RestTemplate restTemplate;

        @GetMapping("/consume")
        public String consumeService() {
            // 使用服务ID调用服务
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://eureka-client/hi", String.class);
            return "Response from service: " + responseEntity.getBody();
        }

        @GetMapping("/choose")
        public String chooseServiceInstance() {
            // 使用LoadBalancerClient选择一个服务实例
            ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-client");
            return "Chosen service: " + serviceInstance.getHost() + ":" + serviceInstance.getPort();
        }
    }
}
