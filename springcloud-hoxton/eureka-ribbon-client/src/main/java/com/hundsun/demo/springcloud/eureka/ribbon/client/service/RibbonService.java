package com.hundsun.demo.springcloud.eureka.ribbon.client.service;

import org.hulei.commom.core.model.dto.ResultDTO;
import org.hulei.commom.core.model.req.SimpleReqDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.ribbon.client.service
 * @className: RibbonService
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 23:04
 */

@Deprecated
@Slf4j
@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    /*
    `@HystrixCommand` 注解是 Netflix 的 Hystrix 框架提供的一种方式，用于对方法进行声明式的熔断、降级、隔离等操作，以增强微服务架构的可靠性和稳定性。下面是该注解的详细解释：
    1. **groupKey**：指定命令所属的组，用于对命令进行分组。默认值是被注解方法的运行时类名。
    2. **commandKey**：指定命令的键名，用于在监控、度量、报警等方面进行识别和区分。默认值是被注解方法的名称。
    3. **threadPoolKey**：指定线程池的键名，用于指定命令执行的线程池。默认情况下，命令使用 Hystrix 默认的线程池。
    4. **fallbackMethod**：指定命令执行失败时调用的回退方法名。回退方法应该在与被注解方法相同的类中，并且方法签名应该与被注解方法相同。
    5. **commandProperties**：指定命令的属性，用于配置 Hystrix 命令的行为，比如设置命令的超时时间、执行是否允许短路、执行超时的等待时间等。
    6. **threadPoolProperties**：指定线程池的属性，用于配置 Hystrix 线程池的行为，比如设置线程池大小、队列大小、拒绝策略等。
    7. **ignoreExceptions**：指定忽略的异常列表，这些异常在触发时会被忽略，不会触发熔断机制。
    8. **observableExecutionMode**：指定命令的执行模式，可以是 EAGER（即时执行）或 LAZY（延迟执行）。默认是 EAGER，即立即执行命令。
    9. **raiseHystrixExceptions**：指定需要包装的异常列表，这些异常会被包装在 HystrixRuntimeException 中，用于在调用者层级处理异常。
    10. **defaultFallback**：指定默认的回退方法名，用于处理所有命令的失败情况。这个方法不能有参数，返回类型应与命令的返回类型兼容。
    通过 `@HystrixCommand` 注解，可以在方法上声明各种命令的行为，包括熔断、降级、隔离等，以增强微服务的容错能力和稳定性。
     */
    @HystrixCommand(fallbackMethod = "hiError")
    public String hi() {

        String url = "http://eureka-client/";
        StringBuffer sb = new StringBuffer();

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

        return sb.toString();
    }

    public String hiError() {
        return "sorry,error!";
    }
}
