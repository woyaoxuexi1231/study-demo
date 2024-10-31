# windows安装

参考文章：[Nacos详细教程_nacos 教程-CSDN博客](https://blog.csdn.net/Top_L398/article/details/111352983)

源码解压后：

```shell
./startup.cmd -m standalone
```



# Docker安装

```shell
# 拉取镜像
docker pull nacos/nacos-server
# https://hub.docker.com/r/nacos/nacos-server 这个网站有介绍使用
# docker run --name nacos-quick -e MODE=standalone -p 8849:8848 -d nacos/nacos-server:2.0.2
# 我们这里不带版本号，也不改端口，并且设置自动重启
docker run --name nacos-quick -e MODE=standalone -p 8848:8848 -d --restart=unless-stopped nacos/nacos-server
```



# 使用

## 作为配置中心

[nacos 配置中心详解（有这一篇就够啦）-CSDN博客](https://blog.csdn.net/qing_zhi_feng/article/details/136363273)

