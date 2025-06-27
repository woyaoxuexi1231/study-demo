[Docker 部署 RocketMQ | RocketMQ](https://rocketmq.apache.org/zh/docs/quickStart/02quickstartWithDocker/)

docker run -d --name rmqnamesrv --restart=unless-stopped -p 9876:9876 --network rocketmq apache/rocketmq sh mqnamesrv