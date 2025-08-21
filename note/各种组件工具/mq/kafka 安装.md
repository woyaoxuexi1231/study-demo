[kafka教程之linux安装教程（一）_linux安装kafka详细教程-CSDN博客](https://blog.csdn.net/weixin_42109071/article/details/107564094)



下载地址

[Apache Kafka](https://kafka.apache.org/downloads)



```bash
tar -zxvf kafka_2.12-2.4.1.tgz 

mkdir -p /root/kafka/kafka_2.13-3.9.1/kafka_data
mkdir -p /root/kafka/kafka_2.13-3.9.1/kafka_data/zookeeper  
mkdir -p /root/kafka/kafka_2.13-3.9.1/kafka_data/log 
mkdir -p /root/kafka/kafka_2.13-3.9.1/kafka_data/log/kafka  
mkdir -p /root/kafka/kafka_2.13-3.9.1/kafka_data/log/zookeeper  

vim server.properties
# 主要修改 log.dirs zookeeper.connect port

vim zookeeper.properties
# 主要修改 dataDir dataLogDir clientPort


# 先启动 zookeeper
sh zookeeper-server-start.sh /root/kafka/kafka_2.13-3.9.1/config/zookeeper.properties

# 启动kafka
sh kafka-server-start.sh /root/kafka/kafka_2.13-3.9.1/config/server.properties
```

