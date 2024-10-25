参考文章: [Admin API | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/admin-api/)

## apisix amdin api的配置

```yaml
deployment:
    admin:
        admin_key:
        - name: admin
        	# 使用默认的 Admin API Key 存在安全风险，部署到生产环境时请及时更新
            key: edd1c9f034335f136f87ad84b625c8f1  
            role: admin
        # http://nginx.org/en/docs/http/ngx_http_access_module.html#allow
        allow_admin:                    
            - 127.0.0.0/24
        admin_listen:
        	# Admin API 监听的 IP，如果不设置，默认为“0.0.0.0”。
            ip: 0.0.0.0
            # Admin API 监听的 端口，必须使用与 node_listen 不同的端口。
            port: 9180                  
```



## 路由配置相关API(Admin API)

### 请求地址

路由资源请求地址：/apisix/admin/routes

### 请求方法

| 名称   | 请求 URI                         | 请求 body | 描述                                                         |
| ------ | -------------------------------- | --------- | ------------------------------------------------------------ |
| GET    | /apisix/admin/routes             | 无        | 获取资源列表。                                               |
| GET    | /apisix/admin/routes/{id}        | 无        | 获取资源。                                                   |
| PUT    | /apisix/admin/routes/{id}        | {...}     | 根据 id 创建资源。                                           |
| POST   | /apisix/admin/routes             | {...}     | 创建资源，id 将会自动生成。                                  |
| DELETE | /apisix/admin/routes/{id}        | 无        | 删除指定资源。                                               |
| PATCH  | /apisix/admin/routes/{id}        | {...}     | 标准 PATCH，修改指定 Route 的部分属性，其他不涉及的属性会原样保留；如果你需要删除某个属性，可以将该属性的值设置为 `null`；当需要修改属性的值为数组时，该属性将全量更新。 |
| PATCH  | /apisix/admin/routes/{id}/{path} | {...}     | SubPath PATCH，通过 `{path}` 指定 Route 要更新的属性，全量更新该属性的数据，其他不涉及的属性会原样保留。两种 PATCH 的区别，请参考使用示例。 |

带有请求参数的API,参数包含如下:

### URI 请求参数

| 名称 | 必选项 | 类型 | 描述                                                         | 示例  |
| ---- | ------ | ---- | ------------------------------------------------------------ | ----- |
| ttl  | 否     | 辅助 | 路由的有效期。超过定义的时间，APISIX 将会自动删除路由，单位为秒。 | ttl=1 |

### body 请求参数

| 名称             | 必选项                         | 类型     | 描述                                                         | 示例值                                               |
| ---------------- | ------------------------------ | -------- | ------------------------------------------------------------ | ---------------------------------------------------- |
| uri              | 是，与 `uris` 二选一。         | 匹配规则 | 除了如 `/foo/bar`、`/foo/gloo` 这种全量匹配外，使用不同 [Router](https://apisix.apache.org/zh/docs/apisix/terminology/router/) 还允许更高级匹配，更多信息请参考 [Router](https://apisix.apache.org/zh/docs/apisix/terminology/router/)。 | "/hello"                                             |
| uris             | 是，不能与 `uri` 二选一。      | 匹配规则 | 非空数组形式，可以匹配多个 `uri`。                           | ["/hello", "/world"]                                 |
| plugins          | 否                             | Plugin   | Plugin 配置，请参考 [Plugin](https://apisix.apache.org/zh/docs/apisix/terminology/plugin/)。 |                                                      |
| script           | 否                             | Script   | Script 配置，请参考 [Script](https://apisix.apache.org/zh/docs/apisix/terminology/script/)。 |                                                      |
| upstream         | 否                             | Upstream | Upstream 配置，请参考 [Upstream](https://apisix.apache.org/zh/docs/apisix/terminology/upstream/)。 |                                                      |
| upstream_id      | 否                             | Upstream | 需要使用的 Upstream id，请参考 [Upstream](https://apisix.apache.org/zh/docs/apisix/terminology/upstream/)。 |                                                      |
| service_id       | 否                             | Service  | 需要绑定的 Service id，请参考 [Service](https://apisix.apache.org/zh/docs/apisix/terminology/service/)。 |                                                      |
| plugin_config_id | 否，不能与 Script 共同使用。   | Plugin   | 需要绑定的 Plugin Config id，请参考 [Plugin Config](https://apisix.apache.org/zh/docs/apisix/terminology/plugin-config/)。 |                                                      |
| name             | 否                             | 辅助     | 路由名称。                                                   | route-test                                           |
| desc             | 否                             | 辅助     | 路由描述信息。                                               | 用来测试的路由。                                     |
| host             | 否，与 `hosts` 二选一。        | 匹配规则 | 当前请求域名，比如 `foo.com`；也支持泛域名，比如 `*.foo.com`。 | "foo.com"                                            |
| hosts            | 否，与 `host` 二选一。         | 匹配规则 | 非空列表形态的 `host`，表示允许有多个不同 `host`，匹配其中任意一个即可。 | ["foo.com", "*.bar.com"]                             |
| remote_addr      | 否，与 `remote_addrs` 二选一。 | 匹配规则 | 客户端请求的 IP 地址。支持 IPv4 地址，如：`192.168.1.101` 以及 CIDR 格式的支持 `192.168.1.0/24`；支持 IPv6 地址匹配，如 `::1`，`fe80::1`，`fe80::1/64` 等。 | "192.168.1.0/24"                                     |
| remote_addrs     | 否，与 `remote_addr` 二选一。  | 匹配规则 | 非空列表形态的 `remote_addr`，表示允许有多个不同 IP 地址，符合其中任意一个即可。 | ["127.0.0.1", "192.0.0.0/8", "::1"]                  |
| methods          | 否                             | 匹配规则 | 如果为空或没有该选项，则表示没有任何 `method` 限制。你也可以配置一个或多个的组合：`GET`，`POST`，`PUT`，`DELETE`，`PATCH`，`HEAD`，`OPTIONS`，`CONNECT`，`TRACE`，`PURGE`。 | ["GET", "POST"]                                      |
| priority         | 否                             | 匹配规则 | 如果不同路由包含相同的 `uri`，则根据属性 `priority` 确定哪个 `route` 被优先匹配，值越大优先级越高，默认值为 `0`。 | priority = 10                                        |
| vars             | 否                             | 匹配规则 | 由一个或多个`[var, operator, val]`元素组成的列表，类似 `[[var, operator, val], [var, operator, val], ...]]`。例如：`["arg_name", "==", "json"]` 则表示当前请求参数 `name` 是 `json`。此处 `var` 与 NGINX 内部自身变量命名是保持一致的，所以也可以使用 `request_uri`、`host` 等。更多细节请参考 [lua-resty-expr](https://github.com/api7/lua-resty-expr)。 | [["arg_name", "==", "json"], ["arg_age", ">", 18]]   |
| filter_func      | 否                             | 匹配规则 | 用户自定义的过滤函数。可以使用它来实现特殊场景的匹配要求实现。该函数默认接受一个名为 `vars` 的输入参数，可以用它来获取 NGINX 变量。 | function(vars) return vars["arg_name"] == "json" end |
| labels           | 否                             | 匹配规则 | 标识附加属性的键值对。                                       | {"version":"v2","build":"16","env":"production"}     |
| timeout          | 否                             | 辅助     | 为 Route 设置 Upstream 连接、发送消息和接收消息的超时时间（单位为秒）。该配置将会覆盖在 Upstream 中配置的 [timeout](https://apisix.apache.org/zh/docs/apisix/admin-api/#upstream) 选项。 | {"connect": 3, "send": 3, "read": 3}                 |
| enable_websocket | 否                             | 辅助     | 当设置为 `true` 时，启用 `websocket`(boolean), 默认值为 `false`。 |                                                      |
| status           | 否                             | 辅助     | 当设置为 `1` 时，启用该路由，默认值为 `1`。                  | `1` 表示启用，`0` 表示禁用。                         |

### 配置路由示例

