package org.hulei.springcloud.apisixadmin;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author hulei
 * @since 2024/10/24 10:29
 */

@Data
public class ApisixAdminReq {
    /**
     * 路由匹配规则 URI
     */
    private String uri;
    /**
     * 路由匹配规则，和uri只能二选一
     */
    private List<String> uris;
    /**
     * 路由 ID，可以由APISIX自动生成
     */
    private String id;
    /**
     * 路由名称
     */
    private String name;
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
    private List<UpstreamNode> nodes;
    /**
     * 超时配置
     */
    private UpstreamTimeout timeout;
    /**
     * 协议
     */
    private String scheme;
}

@Data
class ApisixPlugin {
    /**
     * 路径改写插件
     * https://apisix.apache.org/zh/docs/apisix/plugins/proxy-rewrite/
     */
    @JSONField(name = "proxy-rewrite")
    private ProxyRewritePlugin proxyRewrite;
    /**
     * 限流插件
     * https://apisix.apache.org/zh/docs/apisix/plugins/limit-count/
     */
    @JSONField(name = "limit-count")
    private LimitCountPlugin limitCount;
    /**
     * 用于在执行内置 Lua 插件之前和在 Plugin Runner 内运行特定的 External Plugin。
     * https://apisix.apache.org/zh/docs/apisix/plugins/ext-plugin-pre-req/
     */
    @JSONField(name = "ext-plugin-pre-req")
    private ExtPlugin extPluginPreReq;
    /**
     * 插件将在请求获取到上游的响应之后执行。
     * https://apisix.apache.org/zh/docs/apisix/plugins/ext-plugin-post-resp/
     */
    @JSONField(name = "ext-plugin-post-resp")
    private ExtPlugin extPluginPostResp;
}

@Data
class ProxyRewritePlugin {
    /**
     * 正则改写， 第一个参数为匹配正则表达式, 第二参数为转发路径模版
     * 例如: 第一个参数/demo/* 第二个参数/$1
     * 如果请求为 /demo/hi 那么将转发到 /hi
     */
    @JSONField(name = "regex_uri")
    List<String> regexUri;
    /**
     * 静态改写，此配置标明路径将被改写成什么
     */
    String uri;
}

@Data
class LimitCountPlugin {
    /**
     * 用于检索和增加限制计数的策略
     * "local", "redis", "redis-cluster" 三种方式
     */
    private String policy;
    /**
     * 当请求超过阈值被拒绝时，返回的 HTTP 状态码。
     */
    @JSONField(name = "rejected_code")
    private Integer rejectedCode;
    /**
     * 当请求超过阈值被拒绝时，返回的响应体。
     */
    @JSONField(name = "rejected_msg")
    private String rejectedMsg;
    /**
     * 是否在响应头中显示 X-RateLimit-Limit 和 X-RateLimit-Remaining （限制的总请求数和剩余还可以发送的请求数）
     */
    @JSONField(name = "show_limit_quota_header")
    private Boolean showLimitQuotaHeader;
    /**
     * 当限流插件功能临时不可用时（例如，Redis 超时）是否允许请求继续。当值设置为 true 时则自动允许请求继续
     */
    @JSONField(name = "allow_degradation")
    private Boolean allowDegradation;
    /**
     * 关键字类型，支持：var（单变量）和 var_combination（组合变量）
     * constant: 那么所有请求都共享同一个限流计数器
     * var: 变量,如 $remote_addr会采用限制ip的形式, url参数:arg_??? HttpHeader: http_x_???
     * var_combination: 组合变量
     */
    @JSONField(name = "key_type")
    private String keyType;
    /**
     * 用来做请求计数的有效值。
     * 例如，可以使用主机名（或服务器区域）作为关键字，以便限制每个主机名规定时间内的请求次数。
     * 我们也可以使用客户端地址作为关键字，这样我们就可以避免单个客户端规定时间内多次的连接我们的服务。
     */
    private String key;
    /**
     * 时间窗口, 限流的时间间隔
     */
    @JSONField(name = "time_window")
    private Integer timeWindow;
    /**
     * 每个客户端在指定时间窗口内的总请求数量阈值。
     */
    private Integer count;
}

@Data
class ExtPlugin {
    /**
     * 当限流插件功能临时不可用时（例如，Redis 超时）是否允许请求继续。当值设置为 true 时则自动允许请求继续
     */
    @JSONField(name = "allow_degradation")
    private Boolean allowDegradation;
    /**
     * 配置具体的值
     */
    private List<ExtPluginConfig> conf;
}

@Data
class ExtPluginConfig {
    /**
     * 需要启动的过滤器的名字
     */
    private String name;
    /**
     * 配置值, 后续可以插件中通过 request.getConfig(this); 得到这里配置的值
     */
    private String value;

    public ExtPluginConfig(String name, String value) {
        this.name = name;
        this.value = value;
    }
}

@Data
class UpstreamNode {
    /**
     * 上游节点的IP地址
     */
    private String host;
    /**
     * 上游节点的端口
     */
    private Integer port;
    /**
     * 此上游节点权重占比
     */
    private Integer weight;
}

@Data
class UpstreamTimeout {
    /**
     * 连接超时, 默认6秒
     */
    private Integer connect;
    /**
     * 接收超时, 默认6秒
     */
    private Integer read;
    /**
     * 发送超时, 默认6秒
     */
    private Integer send;
}