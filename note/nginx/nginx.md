# 源码安装



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



# Docker 安装

```sh
docker pull nginx

# 创建挂载目录
mkdir -p /root/nginx/conf
mkdir -p /root/nginx/log
mkdir -p /root/nginx/html

# 先运行一个 nginx，然后把配置文件拿出来，懒得自己手动创建了
# 生成容器
docker run --name nginx -p 80:80 -d nginx
# 将容器nginx.conf文件复制到宿主机
docker cp nginx:/etc/nginx/nginx.conf /root/nginx/conf/nginx.conf
# 将容器conf.d文件夹下内容复制到宿主机
docker cp nginx:/etc/nginx/conf.d /root/nginx/conf/conf.d
# 将容器中的html文件夹复制到宿主机
docker cp nginx:/usr/share/nginx/html /root/nginx/

# 删除这个nginx重新再跑一个有文件挂载的nginx服务
# 直接执行docker rm nginx或者以容器id方式关闭容器
docker stop nginx
# 删除该容器
docker rm nginx
# 删除正在运行的nginx容器
docker rm -f nginx

docker run \
-p 80:80 --restart=unless-stopped \
--name nginx \
-v /root/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /root/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /root/nginx/log:/var/log/nginx \
-v /root/nginx/html:/usr/share/nginx/html \
-d nginx:latest
```



# 配置端口转发

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



在http标签下的server标签内部也可以通过路径配置端口转发

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

