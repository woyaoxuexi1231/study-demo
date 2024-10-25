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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        // ApisixAdminApplication apisixAdminApplication = new ApisixAdminApplication();
        // apisixAdminApplication.buildAdminReqDemo();
    }

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getRoutes")
    public void getRoutes() {
        // 发起 GET 请求，使用 exchange 方法
        String url = "http://192.168.3.233:9180/apisix/admin/routes";
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(buildHeaders()), Object.class);
        prettyPrint(responseEntity);
    }

    @GetMapping("/addRoute")
    public void addRoute() throws JsonProcessingException {
        // System.out.println(restTemplate.getForEntity("http://192.168.3.233:9180/apisix/admin/routes", Object.class));

        RestTemplate restTemplate = new RestTemplate();
        // 创建 HttpEntity，封装请求头
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(buildAdminReqDemo()), buildHeaders());
        // 发起 GET 请求，使用 exchange 方法
        String url = "http://192.168.3.233:9180/apisix/admin/routes";
        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Object.class);
        // 处理响应
        prettyPrint(responseEntity);
    }

    @SneakyThrows
    public ApisixAdminReq buildAdminReqDemo() {

        /* ==============================基础配置============================ */
        ApisixAdminReq apisixAdminReq = new ApisixAdminReq();
        // id让apisix自动生成
        // apisixAdminReq.setId(String.valueOf(System.currentTimeMillis()));
        apisixAdminReq.setName("route-demo-test-" + System.currentTimeMillis());
        apisixAdminReq.setUri("/eureka-client/hi2");
        // 请求类型
        apisixAdminReq.setMethods(CollectionUtil.newArrayList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "CONNECT", "TRACE", "PURGE"));

        /* ==============================上游配置============================ */
        ApisixUpstream apisixUpstream = new ApisixUpstream();
        // 负载均衡算法
        apisixUpstream.setType("roundrobin");
        // 协议
        apisixUpstream.setScheme("http");
        // 配置上游节点
        UpstreamNode node = new UpstreamNode();
        node.setHost("192.168.3.233");
        node.setPort(12101);
        node.setWeight(1);
        apisixUpstream.setNodes(CollectionUtil.newArrayList(node));
        apisixAdminReq.setUpstream(apisixUpstream);
        // 配置超时配置
        // UpstreamTimeout upstreamTimeout = new UpstreamTimeout();

        /* ==============================插件配置============================ */
        // 配置插件
        ApisixPlugin plugin = new ApisixPlugin();
        // 路由重写
        ProxyRewritePlugin proxyRewrite = new ProxyRewritePlugin();
        proxyRewrite.setUri("/hi2");
        plugin.setProxyRewrite(proxyRewrite);

        // 配置限流插件
        LimitCountPlugin limitCountPlugin = new LimitCountPlugin();
        limitCountPlugin.setPolicy("local");
        limitCountPlugin.setRejectedCode(429);
        limitCountPlugin.setRejectedMsg("Too Many Requests");
        limitCountPlugin.setShowLimitQuotaHeader(true);
        limitCountPlugin.setAllowDegradation(false);
        limitCountPlugin.setKeyType("var");
        limitCountPlugin.setKey("arg_user");
        limitCountPlugin.setTimeWindow(60);
        limitCountPlugin.setCount(2);
        plugin.setLimitCount(limitCountPlugin);

        // extPluginPreReq插件
        ExtPlugin extPluginPreReq = new ExtPlugin();
        extPluginPreReq.setAllowDegradation(false);
        extPluginPreReq.setConf(CollectionUtil.newArrayList(new ExtPluginConfig("TokenValidator", "")));
        plugin.setExtPluginPreReq(extPluginPreReq);

        ExtPlugin extPluginPostResp = new ExtPlugin();
        extPluginPostResp.setAllowDegradation(false);
        extPluginPostResp.setConf(CollectionUtil.newArrayList(new ExtPluginConfig("TokenValidator", "")));
        plugin.setExtPluginPostResp(extPluginPostResp);
        // 插件配置完成
        apisixAdminReq.setPlugins(plugin);


        /* ==============================打印以下生成的内容============================ */
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用格式化
        System.out.println(objectMapper.writeValueAsString(JSONObject.toJSON(apisixAdminReq)));

        return apisixAdminReq;
    }


    @SneakyThrows
    public void prettyPrint(ResponseEntity<?> responseEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用格式化
        System.out.println(objectMapper.writeValueAsString(responseEntity.getBody()));
    }

    public HttpHeaders buildHeaders() {
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        // 新增一个 X-API-Key: edd1c9f034335f136f87ad84b625c8f1 请求头, 这是 apisix 权限控制
        headers.set("X-API-Key", "edd1c9f034335f136f87ad84b625c8f1"); // 添加认证头

        return headers;
    }

}
