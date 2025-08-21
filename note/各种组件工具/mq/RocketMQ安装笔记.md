# 源码安装

## rocket安装

[RocketMQ 在Linux上的安装_linux安装rocketmq-CSDN博客](https://blog.csdn.net/xhmico/article/details/122938904)

```
# 启动 nameserver
./mqnamesrv &

# 启动 borker
./mqbroker -n localhost:9876 &
```

## RocketMQ Dashboard

[apache/rocketmq-dashboard: The state-of-the-art Dashboard of Apache RoccketMQ provides excellent monitoring capability. Various graphs and statistics of events, performance and system information of clients and application is evidently made available to the user.](https://github.com/apache/rocketmq-dashboard)





# Docker安装

[Docker 部署 RocketMQ | RocketMQ](https://rocketmq.apache.org/zh/docs/quickStart/02quickstartWithDocker/)

docker run -d --name rmqnamesrv --restart=unless-stopped -p 9876:9876 --network rocketmq apache/rocketmq sh mqnamesrv



