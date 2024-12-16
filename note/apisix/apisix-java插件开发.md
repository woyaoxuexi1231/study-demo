![Apache APISIX 多语言支持架构图](https://static.apiseven.com/apisix-webp/202108/1639464774923-50cebc94-6344-4ea6-88a6-2b424c5f8567.webp)



Java插件开发：

参考文章：

- [插件开发 | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/plugin-develop/)
- [使用 Java 编写 Apache APISIX 插件 | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/blog/2021/06/21/use-java-to-write-apache-apisix-plugins/)

启动 Apache APISIX，然后新增一条路由配置，指定该路由需要调用 apisix-java-plugin-runner 的 `TokenValidator` 插件，示例如下

```shell
curl -i "http://127.0.0.1:9180/apisix/admin/routes" \
-H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' \
-X POST \
-d \
'{
    "uri":"/get",
    "plugins":{
        "ext-plugin-pre-req":{
            "conf":[
                {
                    "name":"TokenValidator",
                    "value":"{\"validate_header\":\"token\",\"validate_url\":\"https://www.sso.foo.com/token/validate\",\"rejected_code\":\"403\"}"
                }
            ]
        }
    },
    "upstream":{
        "nodes":{
            "httpbin.org:80":1
        },
        "type":"roundrobin"
    }
}'
```



配置好之后打包apisix-java-plugin-runner ,部署好之后,curl这个接口

```she
java -jar -DAPISIX_LISTEN_ADDRESS=unix:/tmp/runner.sock -DAPISIX_CONF_EXPIRE_TIME=3600 apisix-java-plugin-runner.jar
```

```shell
curl -H 'token: 123456' 127.0.0.1:9080/get -d '
{
 "args": {},
 "headers": {
 "Accept": "/",
 "Host": "127.0.0.1",
 "Token": "123456",
 "User-Agent": "curl/7.71.1",
 "X-Amzn-Trace-Id": "Root=1-60cb0424-02b5bf804cfeab5252392f96",
 "X-Forwarded-Host": "127.0.0.1"
 },
 "origin": "127.0.0.1",
 "url": "http://127.0.0.1/get"
} '
```

```shell
curl "http://127.0.0.1:9080/get"
```



随笔：

Apache APISIX 相当于 client 端，会监听位于 `/tmp/runner.sock` 位置上的 Unxi Domain Socket 链接。

Unix 域套接字**不需要占用端口**。与 TCP/IP 套接字不同，TCP/IP 套接字使用端口号来标识通信端点，而 Unix 域套接字则使用文件路径。多个进程可以同时使用不同的 Unix 域套接字，而不会产生冲突，因为它们使用的是文件系统中的不同文件。



### apisix插件需要配置

apisix的config.yaml文件，新增如下配置：

```yaml
ext-plugin:
  path_for_test: /tmp/runner.sock
  cmd: ['/usr/local/java/jdk-11.0.12/bin/java', '-jar', '-DAPISIX_LISTEN_ADDRESS=unix:/tmp/runner.sock', '-DAPISIX_CONF_EXPIRE_TIME=3600', '/root/apisix/apisix-java-plugin-runner/apisix-runner-bin/apisix-java-plugin-runner.jar']
  
```

