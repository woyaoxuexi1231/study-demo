https://nginx.org/en/download.html

下载源码包 解压

```shell
# 编译前检查的命令
./configure
# 如果缺少依赖
yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel

# 编译
make
# 安装
make install 

nginx -c /etc/nginx/nginx.conf -s reload
```





### 配置端口转发

stream 可以配置 TCP/UDP 流量转发

```nginx
# 定义 stream 模块的配置
stream {

    upstream mysql_backend {
        server 192.168.3.101:3306;
    }

    server {
        listen 8001;  # 监听端口 3306
        proxy_pass mysql_backend;  # 将流量转发到 upstream mysql_backend
        proxy_timeout 10s;  # 设置代理超时时间
        # error_log /var/log/nginx/mysql_error.log;  # 错误日志
    }
}
```



在http内部也可以通过路径配置端口转发

```nginx

# 服务 1，转发到本地 8080 端口
location /service1/ {
    proxy_pass http://127.0.0.1:10008/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}

# 服务 2，转发到本地 8081 端口
location /service2/ {
    proxy_pass http://127.0.0.1:10009/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

