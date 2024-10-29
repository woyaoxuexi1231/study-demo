package org.hulei.springcloud.client.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: study-demo
 * @package: com.hundsun.demo.springcloud.eureka.client.controller
 * @className: RestTestController
 * @description:
 * @author: h1123
 * @createDate: 2023/5/6 22:21
 */

@Slf4j
@RestController
public class RestTestController {

    @Value(value = "${server.port}")
    private String port;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/testRest")
    public String testRest() {
        return restTemplate.getForObject("https://www.baidu.com", String.class);
    }


    /**
     * 只能发一些比较简单的请求,甚至发不了 req Body
     */
    @GetMapping("/restTemplateGetForObject")
    public void restTemplateGetForObject() {

        String object = restTemplate.getForObject("http://localhost:" + port + "/hi", String.class);
        System.out.println(object);

        // 一个包含 URI 变量的 Map，用于替换 URL 中的占位符。
        Map<String, Object> map = new HashMap<>();
        map.put("req", "123");
        map.put("other", "234");
        System.out.println(restTemplate.getForObject("http://localhost:" + port + "/hi3/{req}/{other}", String.class, map));
        System.out.println(restTemplate.getForObject("http://localhost:" + port + "/hi3/{req}/{other}", String.class, 123, 456));
    }

    @GetMapping("/restTemplateGetForEntity")
    public void restTemplateGetForEntity() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/hi", String.class);
        // 一个包含 URI 变量的 Map，用于替换 URL 中的占位符。
        Map<String, Object> map = new HashMap<>();
        map.put("req", "123");
        map.put("other", "234");
        ResponseEntity<String> forEntity1 = restTemplate.getForEntity("http://localhost:" + port + "/hi3/{req}/{other}", String.class, map);
        log.info("stateCode: {}, body: {}, headers: {}", forEntity1.getStatusCode(), forEntity1.getBody(), forEntity1.getHeaders());
        ResponseEntity<String> forEntity2 = restTemplate.getForEntity("http://localhost:" + port + "/hi3/{req}/{other}", String.class, 123, 456);
        log.info("stateCode: {}, body: {}, headers: {}", forEntity2.getStatusCode(), forEntity2.getBody(), forEntity2.getHeaders());
    }

    @GetMapping("/restTemplateExchange")
    public void restTemplateExchange() {
        // 创建 HttpEntity，封装请求头
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(new Object(){
            String req = "123";
            public String getReq() {
                return req;
            }
            public void setReq(String req) {
                this.req = req;
            }
        }), new HttpHeaders());
        // 发起 GET 请求，使用 exchange 方法
        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://localhost:" + port + "/hi5", HttpMethod.POST, entity, Object.class);
    }
}
