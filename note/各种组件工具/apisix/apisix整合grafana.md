### Prometheus 安装

官网下载安装包：[Download Grafana | Grafana Labs](https://grafana.com/grafana/download)

解压 ./prometheus 可启动

 

端口冲突问题（默认端口9090，很容易冲突）：

nohup ./prometheus --config.file=prometheus.yml --web.listen-address=:9110&



### grafana安装

```shell
yum install -y https://dl.grafana.com/enterprise/release/grafana-enterprise-10.0.1-1.x86_64.rpm

#查看服务状态
systemctl status grafana-server.service
#设置服务开机自启动
systemctl enable grafana-server.service
#启动服务
systemctl start grafana-server.service

# 默认端口3000
## 启动后 http://ip:3000
## 默认的登录用户名/密码：admin/admin
```



apisix提供了相关的文档：

参考文章： [使用 Prometheus 监控云原生 API 网关 APISIX | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/blog/2022/07/13/monitor-api-gateway-apisix-with-prometheus/)



问题：

- apisix-dashboard 内嵌 grafana 页面打不开

  需要新增 apisix-dashboard 的配置（默认可能没打开这个）：

  ```yaml
  conf:
   security:
    content_security_policy: "frame-src *;"
  ```

  修改后重启dashboard
