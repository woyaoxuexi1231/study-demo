# windows安装

参考文章：[Nacos详细教程_nacos 教程-CSDN博客](https://blog.csdn.net/Top_L398/article/details/111352983)

源码解压后：

```shell
./startup.cmd -m standalone
```



# Docker安装

[Docker安装nacos（图文并茂，避免踩坑，一步到位）_docker 安装nacos并且配置文件挂载-CSDN博客](https://blog.csdn.net/web2u/article/details/145107436)

[一文教你使用 Docker 启动并安装 Nacos-阿里云开发者社区](https://developer.aliyun.com/article/1274726)

[版本说明 · alibaba/spring-cloud-alibaba Wiki](https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明)



[Nacos 2.2.1版本 docker启动 缺少NACOS_AUTH_TOKEN等配置获取最新镜像 启动容器 此时容器 - 掘金](https://juejin.cn/post/7213677398720757821)

单机版

```shell
# 拉取镜像
docker pull nacos/nacos-server
# https://hub.docker.com/r/nacos/nacos-server 这个网站有介绍使用

docker network create nacos_network

# 新版本从 1.4.1 版本开始支持鉴权功能
# 方法 1：使用默认的 SecretKey（Nacos 默认鉴权密钥）
# Nacos 默认的 SecretKey 是：SecretKey012345678901234567890123456789012345678901234567890123456789

echo -n "SecretKey012345678901234567890123456789012345678901234567890123456789" | base64

# docker run --name nacos-quick -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2
# 我们这里不带版本号，也不改端口，并且设置自动重启
docker run -d \
--name nacos-standalone \
-p 8848:8848 \
-e MODE=standalone \
-e NACOS_AUTH_ENABLE=false \
nacos/nacos-server:v2.2.1
```



# 使用

## 作为配置中心

[nacos 配置中心详解（有这一篇就够啦）-CSDN博客](https://blog.csdn.net/qing_zhi_feng/article/details/136363273)

