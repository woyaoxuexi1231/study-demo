# 安装

参考安装教程：[APISIX 安装指南 | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/installation-guide/)

1. 更换yum源

   ```shell
   # 备份一下原始的镜像文件
   mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.back
   # 配置阿里云的镜像
   curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
   # 重新生成缓存
   yum makecache
   ```

2. 安装etcd

   ```shell
   
   ```

3. 安装apisix 

   ```shell
   yum install apisix-3.8.0
   # 或者
   yum install apisix
   ```

4. 安装完成后

   ```shell
   # 测试是否成功安装
   curl -sL https://run.api7.ai/apisix/quickstart | sh
   # Server: APISIX/3.9.1
   ```

# 基本使用

```shell
# 1. 添加路由信息和路由转发(node_list)不同, admin接口的端口默认9180
# 需要在头部添加apikey信息，否则接口会401
curl -i "http://127.0.0.1:9180/apisix/admin/routes" \
-X PUT \
-H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" \
-d '{
  "id": "getting-started-ip",
  "uri": "/ip",
  "upstream": {
    "type": "roundrobin",
    "nodes": {
      "httpbin.org:80": 1
    }
  }
}'
# 调用 /ip 接口
curl "http://127.0.0.1:9080/ip"

# 2.配置负载均衡
curl -i "http://127.0.0.1:9180/apisix/admin/routes" \
-X PUT \
-H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" \
-d '{
  "id": "getting-started-headers",
  "uri": "/headers",
  "upstream" : {
    "type": "roundrobin",
    "nodes": {
      "httpbin.org:443": 1,
      "mock.api7.ai:443": 1
    },
    "pass_host": "node",
    "scheme": "https"
  }
}'
# 生成100个请求，查看负载情况
hc=$(seq 100 | xargs -I {} curl "http://127.0.0.1:9080/headers" -sL | grep "httpbin" | wc -l); echo httpbin.org: $hc, mock.api7.ai: $((100 - $hc))


# 对tom用户新增密钥插件
curl -i "http://127.0.0.1:9180/apisix/admin/consumers" -X PUT -H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" -d  '
{
  "username": "tom",
  "plugins": {
    "key-auth": {
      "key": "secret-key"
    }
  }
}'

# 启用 Authentication
curl -i "http://127.0.0.1:9180/apisix/admin/routes/getting-started-ip" -X PATCH -H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" -d '
{
  "plugins": {
    "key-auth": {}
  }
}'

# 后续在调用 /ip 这个接口必须添加请求头 -H 'apikey: wrong-key'

# 禁用 Authentication， 将参数设置 _meta.disable 为 true，即可禁用密钥验证插件。
curl "http://127.0.0.1:9180/apisix/admin/routes/getting-started-ip" -X PATCH -H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" -d '
{
  "plugins": {
    "key-auth": {
      "_meta": {
        "disable": true
      }
    }
  }
}'


# 限流操作
curl -i "http://127.0.0.1:9180/apisix/admin/routes/getting-started-ip" -X PATCH -H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" -d '
{
  "plugins": {
    "limit-count": {
        "count": 2,
        "time_window": 10,
        "rejected_code": 503
     }
  }
}'

# 发送100个请求测试结果
count=$(seq 100 | xargs -I {} curl "http://127.0.0.1:9080/ip" -I -sL | grep "503" | wc -l); echo \"200\": $((100 - $count)), \"503\": $count

# 禁用限流
curl -i "http://127.0.0.1:9180/apisix/admin/routes/getting-started-ip" -X PATCH -H "X-API-Key: edd1c9f034335f136f87ad84b625c8f1" -d '
{
    "plugins": {
        "limit-count": {
            "_meta": {
                "disable": true
            }
        }
    }
}'
```



# 对路由配置limit-count插件

## 页面端配置

编辑路由规则第三步会出现插件配置，选择limit-count插件，进行配置

count: 限定时间窗口内请求数量的限制

time_windows：时间窗口的大小（以秒为单位）

key_type: 

key: 

1. **URL 参数**：例如，用户 ID 作为请求的查询参数传递，如 `/api/resource?user_id=12345`。在这种情况下，你可以使用 `key: "arg_user_id"` 来进行限流，其中 `arg_` 是 Nginx 的标准用法，用来访问 URL 查询参数。
2. **HTTP Header**：如果用户 ID 通过 HTTP 请求头传递，例如 `X-User-ID: 12345`，你可以使用 `key: "http_x_user_id"` 来进行限流。
3. **Cookie**：用户 ID 也可以存储在 cookie 中，例如 `user_id=12345`，这时可以使用 `key: "cookie_user_id"`。

```shell
curl "http://192.168.3.233:9080/ip"
curl "http://127.0.0.1:9080/ip"
```

