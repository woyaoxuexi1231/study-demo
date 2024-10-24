package org.hulei.springcloud.apisixadmin;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hulei
 * @since 2024/10/24 9:49
 */

@RestController
@Slf4j
@SpringBootApplication
public class ApisixAdminApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(ApisixAdminApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/apisixadmin")
    public void apisixadmin() throws JsonProcessingException {
        // System.out.println(restTemplate.getForEntity("http://192.168.3.233:9180/apisix/admin/routes", Object.class));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用格式化

        RestTemplate restTemplate = new RestTemplate();

        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        // 新增一个 X-API-Key: edd1c9f034335f136f87ad84b625c8f1 请求头, 这是 apisix 权限控制
        headers.set("X-API-Key", "edd1c9f034335f136f87ad84b625c8f1"); // 添加认证头

        // 创建请求体body
        ApisixAdminReq apisixAdminReq = buildAdminReq();

        // 创建 HttpEntity，封装请求头
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(apisixAdminReq), headers);

        // 发起 GET 请求，使用 exchange 方法
        String url = "http://192.168.3.233:9180/apisix/admin/routes";
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);

        // 处理响应
        Object responseBody = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();

        System.out.println("Status Code: " + statusCode);
        System.out.println(objectMapper.writeValueAsString(responseBody));
    }

    @SneakyThrows
    public ApisixAdminReq buildAdminReq() {
        ApisixAdminReq apisixAdminReq = new ApisixAdminReq();
        // apisixAdminReq.setId(String.valueOf(System.currentTimeMillis()));
        apisixAdminReq.setName("insert-test1");
        apisixAdminReq.setUri("/eureka-client/*");
        // 请求类型
        apisixAdminReq.setMethods(CollectionUtil.newArrayList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "CONNECT", "TRACE", "PURGE"));

        // 上游配置
        ApisixUpstream apisixUpstream = new ApisixUpstream();
        apisixUpstream.setType("roundrobin");
        Map<String, Integer> map = new HashMap<>();
        map.put("192.168.3.233:12101", 1);
        apisixUpstream.setNodes(map);
        apisixAdminReq.setUpstream(apisixUpstream);

        // 配置插件
        ApisixPlugin plugin = new ApisixPlugin();
        ProxyRewrite proxyRewrite = new ProxyRewrite();
        proxyRewrite.setRegexUri(CollectionUtil.newArrayList("/eureka-client/*", "/$1"));
        plugin.setProxyRewrite(proxyRewrite);
        apisixAdminReq.setPlugins(plugin);


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用格式化
        System.out.println(objectMapper.writeValueAsString(JSONObject.toJSON(apisixAdminReq)));

        return apisixAdminReq;
    }
}
