package org.hulei.springcloud.apisixadmin;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hulei
 * @since 2024/10/24 10:29
 */

@Data
public class ApisixAdminReq {
    /**
     * 路由 ID
     */
    private String id;
    /**
     * 路由名称
     */
    private String name;
    /**
     * 路由匹配规则 URI
     */
    private String uri;
    /**
     * 支持的 HTTP 方法
     * "methods" : [ "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "CONNECT", "TRACE", "PURGE" ]
     */
    private List<String> methods;
    /**
     * 上游服务配置
     */
    private ApisixUpstream upstream;
    /**
     * 插件配置
     */
    private ApisixPlugin plugins;
}

@Data
class ApisixUpstream {
    /**
     * 负载均衡算法
     * 默认是 roundrobin,
     */
    private String type;
    /**
     * 上游节点
     */
    private Map<String, Integer> nodes;
}

@Data
class ApisixPlugin {
    @JSONField(name = "proxy-rewrite")
    private ProxyRewrite proxyRewrite;
}

@Data
class ProxyRewrite {
    @JSONField(name = "regex_uri")
    List<String> regexUri;
}