## 集群搭建

在三台服务器上的 zoo.cfg 配置文件都添加:

```
server.1=192.168.80.128:2888:3888
server.2=192.168.80.129:2888:3888
server.3=192.168.80.130:2888:3888
```

在每台机器的data目录添加 myid 文件,文件内容分别为 1,2,3

## shell脚本

```shell
#!/bin/bash
#chkconfig:2345 20 90
#description:zookeeper
#processname:zookeeper

export PATH=$JAVA_HOME/bin:$PATH

case $1 in
        start) su root /root/zookeeper-3.8.0/bin/zkServer.sh start;;
        stop) su root /root/zookeeper-3.8.0/bin/zkServer.sh stop;;
        status) su root /root/zookeeper-3.8.0/bin/zkServer.sh status;;
        restart) su root /root/zookeeper-3.8.0/bin/zkServer.sh restart;;
        *) echo "require start|stop|status|restart" ;;
esac
```