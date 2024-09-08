package com.hundsun.demo.springcloud.loadbalancer;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.model.req.SimpleReqDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
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
        // return new RestTemplateBuilder().rootUri("http://eureka-client/").build();
        return new RestTemplate();
    }

    @RestController
    static class LoadBalancedController {

        String url = "http://eureka-client/";

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Autowired
        LoadBalancedController loadBalancedController;

        @Autowired
        private RestTemplate restTemplate;

        @GetMapping("/consume")
        public String consumeService() {

            StringBuilder sb = new StringBuilder();

            // 无参格式
            sb.append(restTemplate.getForObject(url + "hi", String.class)).append("\n");

            sb.append(restTemplate.getForObject(url + "hi2?req={1}", ResultDTO.class, "hello")).append("\n");

            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("req", "hello");
            reqMap.put("other", "ps");
            reqMap.put("reqString", "hello");
            sb.append(restTemplate.getForObject(url + "hi3/{req}/{other}", ResultDTO.class, reqMap)).append("\n");

            SimpleReqDTO req = new SimpleReqDTO();
            req.setReqString("hello");

            /*
            To pass an object in restTemplate get method, you can use the UriComponentsBuilder class to build the URL with query parameters. Here is an example:
            RestTemplate restTemplate = new RestTemplate();
            URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/endpoint")
                .queryParam("param1", "value1")
                .queryParam("param2", "value2")
                .build()
                .toUri();
            MyObject myObject = restTemplate.getForObject(uri, MyObject.class);

            You can also use the HttpEntity class to pass the object as a request parameter. Here is an example:
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            MyObject myObject = new MyObject("value1", "value2");
            HttpEntity<MyObject> entity = new HttpEntity<>(myObject, headers);
            MyObject response = restTemplate.exchange(
                "http://localhost:8080/api/endpoint?param1={param1}&param2={param2}",
                HttpMethod.GET,
                entity,
                MyObject.class,
                myObject.getParam1(),
                myObject.getParam2()
            ).getBody();
             */
            // 这里搞了很久, 不知道怎么才能传递对象, 这个用 chatGPT 搜出来的结果, 点个赞!
            URI uri = UriComponentsBuilder.fromUriString(url + "hi4")
                    .queryParam("reqString", "hello")
                    .build()
                    .toUri();
            sb.append(restTemplate.getForObject(uri, ResultDTO.class)).append("\n");

            sb.append(restTemplate.getForEntity(url + "hi2?req={1}", ResultDTO.class, "hello")).append("\n");

            // 使用服务ID调用服务
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://eureka-client/hi", String.class);
            sb.append("Response from service: ").append(responseEntity.getBody());

            return sb.toString();
        }

        @GetMapping("/choose")
        public String chooseServiceInstance() {
            // 使用LoadBalancerClient选择一个服务实例
            ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-client");
            return "Chosen service: " + serviceInstance.getHost() + ":" + serviceInstance.getPort();
        }

        @Autowired
        private DiscoveryClient discoveryClient;

        @GetMapping("/invoke-service")
        public String invokeService() {
            // 获取服务实例的信息
            List<ServiceInstance> instances = discoveryClient.getInstances("eureka-client");
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
