# 配置apisix接入nacos

配置 apisix 的 config.yaml 配置文件，追加以下内容：

```yaml
discovery:
  nacos:
    # nacos服务的连接地址
    host:
      - "http://${username}:${password}@${host1}:${port1}"
    # 用于指定 Nacos API 请求的前缀路径。默认为 /nacos/v1/，这是 Nacos 默认的 API 路径。
    prefix: "/nacos/v1/"
    # 指定 APISIX 定期从 Nacos 中拉取服务实例信息的时间间隔，以秒为单位。默认值是 30 秒。
    fetch_interval: 30
    # 指定从 Nacos 服务发现中获取的服务实例的默认权重。这个权重用于负载均衡策略，默认值是 100。
    weight: 100          
    timeout:
      # 指定与 Nacos 服务器建立连接的超时时间，单位是毫秒，默认值是 2000 毫秒。
      connect: 2000      
      # 指定向 Nacos 服务器发送请求的超时时间，单位是毫秒，默认值是 2000 毫秒。
      send: 2000     
      # 指定从 Nacos 服务器读取响应的超时时间，单位是毫秒，默认值是 5000 毫秒。
      read: 5000        
```

