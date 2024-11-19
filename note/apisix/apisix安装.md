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

   github可以下载源码 https://github.com/etcd-io/etcd

   ```shell
   # 解压
   tar -xzvf etcd-v3.4.34-linux-amd64.tar.gz
   
   cd etcd-v3.4.34-linux-amd64
   
   nohup ./etcd >/tmp/etcd.log 2>&1 &
   ```

3. 安装apisix 

   yum 安装

   ```shell
   yum install apisix-3.8.0
   # 或者
   yum install apisix
   ```

   源码安装 https://github.com/apache/apisix/releases

   ```shell
   切换到 APISIX 源码的目录，创建依赖项并安装 APISIX，命令如下所示：
   cd apisix-${APISIX_VERSION}
   make deps
   make install 
   ```
   
4. 安装完成后

   ```shell
   #APISIX 安装完成后，你可以运行以下命令初始化 NGINX 配置文件和 etcd：
   apisix init
   
   #使用以下命令启动 APISIX：
   apisix run
   
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



# 端口



apisix.node_listen 路由监听端口，请求都通过这个端口进行转发

plugin_attr.prometheus.export_addr.port 配置 Prometheus 插件的监控数据导出端口

deployment.admin.admin_listen.port 是用于配置 **Admin API** 的监听端口
