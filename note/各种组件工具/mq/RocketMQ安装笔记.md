# 源码安装

## rocket安装

[RocketMQ 在Linux上的安装_linux安装rocketmq-CSDN博客](https://blog.csdn.net/xhmico/article/details/122938904)

```shell
# 启动前修改启动脚本内的 jvm 内存参数，默认很大

# 启动 nameserver
./mqnamesrv &

# 启动 borker
./mqbroker -n localhost:9876 &
```

### 修改默认端口号

rocketmq config目录下，创建 `namesrv.properties` 文件

```properties
listenPort=8876
```

启动时指定文件，可以修改默认端口号

```bash
./mqnamesrv -c ../conf/namesrv.properties &
```

broker端口也可以进行配置，如果 namesrv 的端口改了，broker 也需要进行修改。

`broker.conf`

```conf
namesrvAddr=127.0.0.1:8876
listenPort=8911
```

同样的，启动时指定配置文件

```bash
./mqbroker -n 127.0.0.1:8876 -c ../conf/broker.conf &
```







## RocketMQ Dashboard

[apache/rocketmq-dashboard: The state-of-the-art Dashboard of Apache RoccketMQ provides excellent monitoring capability. Various graphs and statistics of events, performance and system information of clients and application is evidently made available to the user.](https://github.com/apache/rocketmq-dashboard)





# Docker安装

[Docker 部署 RocketMQ | RocketMQ](https://rocketmq.apache.org/zh/docs/quickStart/02quickstartWithDocker/)

[基于Docker安装RockerMQ【保姆级教程、内含图解】_docker安装rocket mq-CSDN博客](https://blog.csdn.net/Acloasia/article/details/130548105)



创建 `nameserv`

```bash
mkdir -p /root/rocketmq/docker/data/namesrv/logs 
mkdir -p /root/rocketmq/docker/data/namesrv/store

# namesrv 启动
# sh mqnamesrv 当容器启动时，它会运行 sh mqnamesrv 这个命令来启动 Name Server 服务。
docker run -d \
--network rocketmq \
--restart=unless-stopped \
--name rmqnamesrv \
--privileged=true \
-p 9876:9876 \
-v /root/rocketmq/docker/data/namesrv/logs:/root/logs \
-v /root/rocketmq/docker/data/namesrv/store:/root/store \
-e "MAX_POSSIBLE_HEAP=100000000" \
-e "JAVA_OPT_EXT=-Xms512M -Xmx512M -Xmn128m" \
apache/rocketmq \
sh mqnamesrv
```



创建 `broker`

```bash
mkdir -p /root/rocketmq/docker/data/broker/logs 
mkdir -p /root/rocketmq/docker/data/broker/store
mkdir -p /root/rocketmq/docker/conf
cat > /root/rocketmq/docker/conf/broker.conf <<EOF
brokerClusterName = DefaultCluster
brokerName = broker-a
brokerId = 0
deleteWhen = 04
fileReservedTime = 48
brokerRole = ASYNC_MASTER
flushDiskType = ASYNC_FLUSH
brokerIP1 = 192.168.3.102
diskMaxUsedSpaceRatio=95
EOF

# namesrv 启动
# sh mqnamesrv 当容器启动时，它会运行 sh mqnamesrv 这个命令来启动 Name Server 服务。
docker run -d \
--network rocketmq \
--restart=unless-stopped \
--name rmqbroker \
--link rmqnamesrv:namesrv \
--privileged=true \
-p 10911:10911 \
-p 10912:10912 \
-p 10909:10909 \
-v /root/rocketmq/docker/data/broker/logs:/root/logs \
-v /root/rocketmq/docker/data/broker/store:/root/store \
-v /root/rocketmq/docker/conf/broker.conf:/home/rocketmq/conf/broker.conf \
-e "NAMESRV_ADDR=namesrv:9876" \
-e "JAVA_OPT_EXT=-Xms512M -Xmx512M -Xmn128m" \
-e "MAX_POSSIBLE_HEAP=200000000" \
apache/rocketmq \
sh mqbroker -c /home/rocketmq/conf/broker.conf
```







