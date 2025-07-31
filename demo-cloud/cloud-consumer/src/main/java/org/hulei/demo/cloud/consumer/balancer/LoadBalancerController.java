package org.hulei.demo.cloud.consumer.balancer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.hulei.util.dto.ResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author hulei
 * @since 2025/7/31 14:07
 */

@RequestMapping("/balancer")
@RestController
public class LoadBalancerController {

    String url = "http://netflix-provider/client/";
    // String url = "";

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    LoadBalancerController loadBalancedController;

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
