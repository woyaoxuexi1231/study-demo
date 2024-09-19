package org.hulei.keeping.server.spring.restful;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hulei.keeping.server.KeepingApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hulei42031
 * @since 2024-03-29 17:11
 */

@Slf4j
@RestController
@RequestMapping("/restful")
public class RestfulController {

    @Value("${server.port}")
    String port;

    @Autowired
    ThreadPoolExecutor commonPool;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/sendGet")
    public void sendGet() {
        String baseUrl = String.format("http://localhost:%s/restful/getPrint", port);

        // 构建URL，并添加参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("param1", "value1")
                .queryParam("name", "value2");

        // getForEntity(): 返回一个ResponseEntity对象，其中包含完整的HTTP响应信息，包括状态码、头部信息和响应体。
        // getForObject(): 直接返回响应体中的对象，而不包含完整的HTTP响应信息。
        // getForEntity(): 适用于需要获取完整HTTP响应信息的场景，包括状态码、头部信息等。你可以通过ResponseEntity对象来访问这些信息，并根据需要处理。
        // getForObject(): 适用于你只关心响应体中的数据，而不需要额外的HTTP响应信息的场景。这样可以更简洁地获取到你需要的数据。
        // 发起GET请求并获取响应
        String url = builder.toUriString();
        // String response = restTemplate.getForObject(url, String.class);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);

        // 处理响应
        System.out.println("Response: " + forEntity);
    }

    @GetMapping("/sendPost")
    public void sendPost() {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置请求体
        String requestBody = "{\"name\": \"value\"}";
        // 创建HTTP实体对象并设置请求头和请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 发起POST请求并获取响应
        String url = String.format("http://localhost:%s/restful/postPrint", port);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        // 获取响应体
        String responseBody = responseEntity.getBody();
        // 处理响应
        System.out.println("Response: " + responseBody);
    }

    @GetMapping("/getPrint")
    public String getPrint(String name) {
        return String.format("hello %s", name);
    }

    @PostMapping("/postPrint")
    public String postPrint(@RequestBody PostReq req) {
        return String.format("hello %s", req.getName());
    }

    @Data
    static class PostReq {
        String name;
    }

}
