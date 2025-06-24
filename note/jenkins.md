[Jenkins - 安装并启动Jenkins - hanease - 博客园](https://www.cnblogs.com/hanease/p/18677103)

[11-自动化发布到测试服务器并自动运行_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1bS4y1471A/?spm_id_from=333.1391.0.0&p=11&vd_source=8d7ce9dd45b35258ee11a3c3ce982ea9)



# 各种报错

## AWT 报错

报错  AWT is not properly configured on this server. Perhaps you need to run your container with "-Djava.awt.headless=true"? See also: https://www.jenkins.io/redirect/troubleshooting/java.awt.headless

[【Jenkins】Jenkins启动报错：AWT is not properly configured on this server.-CSDN博客](https://blog.csdn.net/jiangjun_dao519/article/details/125620237)

## 安装插件 SSL 报错

[Jenkins安装后，安装插件失败，报错Caused: java.io.IOException: Failed to download from 和SunCertPathBuilderException - 乌鸦哥 - 博客园](https://www.cnblogs.com/crowbrother/p/13789230.html)

## publish over ssh 插件

- Source files 需要上传的源文件，路径是当前的流水线的工作目录，这个工作目录应该是基于 pom 文件来的

  spring-boot流水线的 source files 填写的是 spring/spring-boot/target/spring-boot.jar

- Remove prefix 需要移除的前缀信息，这里如果不填写，那么按照上面的 source files 填写的信息那么上传后的文件路径会在 ssh 服务器(在全局配置中会配置 ssh 服务器信息和工作目录)的工作目录下的 spring/spring-boot/target/spring-boot.jar 
- Remote directory 指定远程的目录信息，这个填写后和Remove prefix作用相反，填写后上传文件会在 ssh 服务器的工作目录下添加这个指定的文件夹前缀
- Exec command 命令行操作，这个命令行的工作目录有点迷，我这里ssh服务器以root用户登录的，只执行pwd这里会显示直接在 /root 目录下，而且！**脚本中 nohup java -jar "$JAR_FILE" > /dev/null 2>&1 & 这个命令无法执行**，[大坑，jenkins 配置publish over ssh bash 插件，远程部署，无法执行shell脚本 - 简书](https://www.jianshu.com/p/c06007175402) 这篇文章有解决方案，需要在命令行第一句执行 . /etc/profile 就可以了。



这是一个通用的启动jar包的脚本，具体jar包名字通过命令行传入

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





# **暂记**

jenkins在安装完成后，数据都会保存在 .jenkins 中， root用户安装的话会在 /root/.jenkins 文件夹中



账户

admin bce9c6402e7247009fb6b97e9220fbf9

