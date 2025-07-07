# 安装

Jenkins 版本为：Jenkins v2.462.3

jdk 版本：jdk 11

[Jenkins - 安装并启动Jenkins - hanease - 博客园](https://www.cnblogs.com/hanease/p/18677103)

[11-自动化发布到测试服务器并自动运行_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1bS4y1471A/?spm_id_from=333.1391.0.0&p=11&vd_source=8d7ce9dd45b35258ee11a3c3ce982ea9)



# Dokcer安装Jenkins

[使用 Docker 安装 Jenkins 并实现项目自动化部署-阿里云开发者社区](https://developer.aliyun.com/article/892646)



```
docker pull jenkins/jenkins:2.452-jdk11

mkdir -p /root/jenkins_mount
chmod 777 /root/jenkins_mount

docker run -d \
-p 8080:8080 \
-p 50000:50000 \
-v /root/jenkins_docker:/var/jenkins_home \
-e TZ=Asia/Shanghai \
--name myjenkins \
--restart=unless-stopped \
jenkins/jenkins:2.462.3-jdk11
```

## docker内部的 java环境配置 git环境配置 maven环境配置

```sh
# Java环境这里直接使用容器内部的 jdk, 这样直接找到 jdk位置
echo $JAVA_HOME 
```

git 在 docker 版本的 jenkins 容器中自带有  maven 直接选择自动安装一个

## maven依赖下载很慢

在容器内生成一份setting配置文件，然后在全局工具配置 使用这个配置文件

```sh
cat > settings-default.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <pluginGroups>
  </pluginGroups>

  <proxies>
  </proxies>
  
  <mirrors>
    <mirror>
      <!--This sends everything else to /public -->
      <id>aliyun</id>
      <mirrorOf>*</mirrorOf>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
  </mirrors>

  <profiles>
  </profiles>
</settings>

EOF
```



# 各种报错

## AWT 报错

报错  AWT is not properly configured on this server. Perhaps you need to run your container with "-Djava.awt.headless=true"? See also: https://www.jenkins.io/redirect/troubleshooting/java.awt.headless

[【Jenkins】Jenkins启动报错：AWT is not properly configured on this server.-CSDN博客](https://blog.csdn.net/jiangjun_dao519/article/details/125620237)

## 安装插件 SSL 报错

[Jenkins安装后，安装插件失败，报错Caused: java.io.IOException: Failed to download from 和SunCertPathBuilderException - 乌鸦哥 - 博客园](https://www.cnblogs.com/crowbrother/p/13789230.html)

## publish over ssh 插件 SSH: Transferred 0 file(s)

主要还是 Source files 不对

- Source files 需要上传的源文件，路径是当前的流水线的工作目录，这个工作目录应该是基于 pom 文件来的

  spring-boot流水线的 source files 填写的是 spring/spring-boot/target/spring-boot.jar

- Remove prefix 需要移除的前缀信息，这里如果不填写，那么按照上面的 source files 填写的信息那么上传后的文件路径会在 ssh 服务器(在全局配置中会配置 ssh 服务器信息和工作目录)的工作目录下的 spring/spring-boot/target/spring-boot.jar 
- Remote directory 指定远程的目录信息，这个填写后和Remove prefix作用相反，填写后上传文件会在 ssh 服务器的工作目录下添加这个指定的文件夹前缀
- Exec command 命令行操作，这个命令行的工作目录有点迷，我这里ssh服务器以root用户登录的，只执行pwd这里会显示直接在 /root 目录下，而且！**脚本中 nohup java -jar "$JAR_FILE" > /dev/null 2>&1 & 这个命令无法执行**，[大坑，jenkins 配置publish over ssh bash 插件，远程部署，无法执行shell脚本 - 简书](https://www.jianshu.com/p/c06007175402) 这篇文章有解决方案，需要在命令行第一句执行 . /etc/profile 就可以了。



# **笔记**

jenkins在安装完成后，数据都会保存在 .jenkins 中， root用户安装的话会在 /root/.jenkins 文件夹中



账户

admin bce9c6402e7247009fb6b97e9220fbf9



## 使用 publish over  ssh 插件

### 直接上传jar包然后后台运行

springboot项目为例：

Source files

```
spring/spring-boot/target/spring-boot.jar
```

Remove prefix

```
spring/spring-boot/target/
```

Remote directory

```
```

Exec command

```
. /etc/profile
cd /root/jenkins/uploads
sh restart.sh spring-boot.jar
```



restart.sh脚本如下，这是一个通用的启动脚本，具体jar包名字通过命令行传入

```shell
#!/bin/bash

# 调试信息（使用时可删除）
echo "Debug: Received $# arguments"
echo "Debug: Arguments are '$@'"

# 参数检查
if [ $# -eq 0 ]; then
  echo "Error: No JAR file specified"
  echo "Usage: $0 <JAR_FILE>"
  exit 1
fi

JAR_FILE="$1"

# 检查文件是否存在
if [ ! -f "$JAR_FILE" ]; then
  echo "Error: Jar file not found: $JAR_FILE"
  exit 1
fi

# 终止旧进程
echo "Stopping existing processes..."
PIDS=$(ps aux | grep "$JAR_FILE" | grep -v grep | grep -v restart.sh | awk '{print $2}')

if [ -z "$PIDS" ]; then
  echo "No running processes found"
else
  for PID in $PIDS; do
    echo "Killing process $PID"
    kill $PID
    sleep 3
    if ps -p $PID >/dev/null; then
      echo "Force killing $PID"
      kill -9 $PID
    fi
  done
fi

# 启动新进程
echo "Starting new process..."
nohup java -jar "$JAR_FILE" >/dev/null 2>&1 &
NEW_PID=$!

sleep 1
if ps -p $NEW_PID >/dev/null; then
  echo "Successfully started with PID $NEW_PID"
else
  echo "Failed to start process"
  exit 1
fi
```



### 使用docker外挂jar包运行

以eureka-server为例

Source files

```
spring/spring-cloud-netflix/netflix-eureka-server/target/netflix-eureka-server.jar
```

Remove prefix

```
spring/spring-cloud-netflix/netflix-eureka-server/target/
```

Remote directory

```
```

Exec command

```
docker stop eureka-server
docker rm eureka-server
docker rmi eureka-server
cd /root/jenkins/uploads
docker run -d \
-p 10001:10001 \
--name eureka-server \
-v /root/jenkins/uploads/netflix-eureka-server.jar:/eureka-server.jar \
--restart=unless-stopped \
openjdk:11 java -jar eureka-server.jar
```

相当于仅使用openjdk:11这个进行来创建容器，jar包在宿主机上而不在docker镜像内



这里对于docker内的jar包向注册中心注册时，我这里都统一使用 host 网络模式 以及指定宿主机ip的形式注册

dubbo + nacos

```
docker stop dubbo-provider-service
docker rm dubbo-provider-service
docker rmi dubbo-provider-service
cd /root/jenkins/uploads
docker run -d \
--network host \
--name dubbo-provider-service \
-v /root/jenkins/uploads/dubbo-provider-service.jar:/dubbo-provider-service.jar \
--restart=unless-stopped \
openjdk:11 \
java -jar -Ddubbo.protocol.host=192.168.3.102 -Ddubbo.application.qos-host=192.168.3.102 dubbo-provider-service.jar
```

springcloudalibaba + nacos

```
docker stop alibaba-nacos-config
docker rm alibaba-nacos-config
docker rmi alibaba-nacos-config
cd /root/jenkins/uploads
docker run -d \
--network host \
--name alibaba-nacos-config \
-v /root/jenkins/uploads/alibaba-nacos-config.jar:/alibaba-nacos-config.jar \
--restart=unless-stopped \
openjdk:11 java -jar alibaba-nacos-config.jar
```

springcloud + consul

```
docker stop leyton-consul-provider
docker rm leyton-consul-provider
docker rmi leyton-consul-provider
cd /root/jenkins/uploads
docker run -d \
--network host \
--name leyton-consul-provider \
-v /root/jenkins/uploads/leyton-consul-provider.jar:/leyton-consul-provider.jar \
--restart=unless-stopped \
openjdk:11 java -jar leyton-consul-provider.jar
```





### 使用docker打包jar镜像运行

