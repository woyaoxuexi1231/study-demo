package com.hundsun.demo.springcloud.eureka.ribbon.client.service;

import com.hundsun.demo.commom.core.model.dto.ResultDTO;
import com.hundsun.demo.commom.core.model.req.SimpleReqDTO;
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

@Slf4j
@Service
public class RibbonService {

    @Autowired
    private RestTemplate restTemplate;

    // @HystrixCommand(fallbackMethod = "hiError")
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
