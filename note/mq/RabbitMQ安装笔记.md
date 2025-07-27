# 单机安装

[linux单机安装手册](https://blog.csdn.net/qq_45173404/article/details/116429302)

```shell

# linux启动脚本

#!/bin/bash
#chkconfig:2345 61 61

export HOME=/root/rabbit/rabbitmq_server-3.8.15
export PATH=$PATH:/root/rabbit/rabbitmq_server-3.8.15/sbin

cd /root/rabbit/rabbitmq_server-3.8.15

case "$1" in
    start)
    echo "Starting RabbitMQ ..."
    rabbitmq-server  -detached
    ;;
stop)
    echo "Stopping RabbitMQ ..."
    rabbitmqctl stop
    ;;
status)
    echo "Status RabbitMQ ..."
    rabbitmqctl status
    ;;
restart)
    echo "Restarting RabbitMQ ..."
    rabbitmqctl stop
    rabbitmq-server  restart
    ;;

*)
    echo "Usage: $prog {start|stop|status|restart}"
    ;;
esac
exit 0

```

# 集群安装

[集群安装手册](https://www.cnblogs.com/caoweixiong/p/14371487.html)

`rabbitmqctl` 命令行工具中的 `set_policy` 和 `set_parameter` 命令都用于配置 RabbitMQ 的参数，但它们的作用和使用方式略有不同。

1. `rabbitmqctl set_policy`:

- 用途：用于设置策略（policy），可以用于定义队列、交换机、虚拟主机等对象的行为规则。
- 参数：通常用于设置队列、交换机等对象的高可用性、过期时间、最大长度等规则。
- 示例：可以用来定义队列的镜像模式、优先级、长度限制等策略。

2. `rabbitmqctl set_parameter`:

- 用途：用于设置 RabbitMQ 服务器级别的参数，例如集群节点间的同步策略、内存限制、日志级别等。
- 参数：通常用于设置 RabbitMQ 服务器的全局配置参数。
- 示例：可以用来设置集群节点的网络同步模式、内存限制、日志级别等。

总的来说，`set_policy` 更加专注于设置对象级别的行为规则，而 `set_parameter` 则更适合设置 RabbitMQ 服务器级别的全局配置参数。

---

```shell
rabbitmqctl set_policy mirror_queue "^topic-" '{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}' --priority 0 --apply-to queues
```

这个命令的作用是设置一个名为 "mirror_queue" 的策略，该策略应用于所有以 "topic-" 开头的队列。具体来说，这个命令会做以下几件事情：

1. `rabbitmqctl set_policy` 命令用于设置策略。
2. `mirror_queue` 是你为策略指定的名称。
3. `^topic-` 是一个正则表达式模式，指定了应用策略的队列名称的匹配规则。在这个例子中，策略将应用到所有以 "topic-" 开头的队列。
4. `{"ha-mode":"exactly","ha-params":2,"ha-sync-mode":"automatic"}` 是策略的配置参数。具体来说：
    - `"ha-mode":"exactly"` 指定了策略的高可用性模式为确切镜像（exactly mirrored）。这意味着队列将在指定数量的节点上进行确切的镜像。
    - `"ha-params":2` 指定了镜像节点的数量为 2。这意味着每个队列会在集群中的两个节点上进行镜像。
    - `"ha-sync-mode":"automatic"` 指定了镜像节点的同步模式为自动同步。这意味着镜像节点将自动同步消息。
5. `--priority 0` 指定了策略的优先级为 0。这意味着这个策略将按照优先级顺序应用。
6. `--apply-to queues` 指定了策略应用到队列。

---



# Docker 

[使用 Docker 部署 RabbitMQ 的详细指南_docker部署rabbitmq-CSDN博客](https://blog.csdn.net/Li_WenZhang/article/details/141181632)

[Docker安装RabbitMQ（以及访问15672端口失败）_docker 安装 rabbitmq telnet 15672失败 5672可以连接-CSDN博客](https://blog.csdn.net/zwb_dzw/article/details/110354356)



```shell
docker run -d \
--restart=always \
--name=rabbitmq-1 \
-v /usr/local/docker/rabbitmq:/var/lib/rabbitmq \
-p 15672:15672 -p 5672:5672 -p 25672:25672 \
--network rabbitmq \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
-e TZ=Asia/Shanghai \
rabbitmq

docker run -d \
--restart=always \
--name=rabbitmq-2 \
-v /usr/local/docker/rabbitmq2:/var/lib/rabbitmq \
-p 15673:15672 -p 5673:5672 -p 25673:25672 \
--network rabbitmq \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin \
-e TZ=Asia/Shanghai \
rabbitmq



# 加入集群
rabbitmqctl join_cluster rabbit@a3fdc1df2f19
```





使用 docker-compose 部署两个rabbitmq 

```yml
version: '3.8'

services:
  rabbitmq-node1:
    image: rabbitmq
    container_name: rabbitmq-node1
    hostname: rabbitmq-node1
    networks:
      - rabbitmq
    ports:
      - "5672:5672"    # AMQP 端口（客户端连接）
      - "15672:15672"  # 管理界面端口
      - "25672:25672"  # 集群通信端口
    environment:
      - RABBITMQ_ERLANG_COOKIE=/root/rabbitmq/cookie/.erlang.cookie  # 挂载 Cookie 路径
      - RABBITMQ_DEFAULT_USER=admin  # 默认管理员账号（可选）
      - RABBITMQ_DEFAULT_PASS=admin  # 默认密码（可选）
    volumes:
      - ./root/rabbitmq/node1:/var/lib/rabbitmq  # 持久化数据目录（宿主机路径）
      - /root/rabbitmq/cookie:/root/rabbitmq/cookie  # 挂载共享 Cookie
    healthcheck:  # 健康检查（确保节点启动完成后再加入集群）
      test: ["CMD", "rabbitmqctl", "status"]
      interval: 10s
      timeout: 5s
      retries: 10

  rabbitmq-node2:
    image: rabbitmq
    container_name: rabbitmq-node2
    hostname: rabbitmq-node2
    networks:
      - rabbitmq
    ports:
      - "5673:5672"    # 避免端口冲突（宿主机映射不同端口）
      - "15673:15672"
      - "25673:25672"
    environment:
      - RABBITMQ_ERLANG_COOKIE=/root/rabbitmq/cookie/.erlang.cookie
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
      - RABBITMQ_CLUSTER_DISCOVERY_SERVICE=rabbitmq-node1  # 自动发现第一个节点（可选）
    volumes:
      - ./root/rabbitmq/node2:/var/lib/rabbitmq
      - /root/rabbitmq/cookie:/data/rabbitmq/cookie
    depends_on:
      rabbitmq-node1:
        condition: service_healthy  # 等待 node1 健康后再启动
    healthcheck:
      test: ["CMD", "rabbitmqctl", "status"]
      interval: 10s
      timeout: 5s
      retries: 10

networks:
  rabbitmq:
    external: true  # 使用已创建的自定义网络
```



```shell
# 进入 node2 容器
docker exec -it rabbitmq-node2 bash

# 进入容器后
rabbitmq-plugins enable rabbitmq_management

rabbitmqctl stop_app

# 加入集群（node1 的主机名或 IP:端口）
rabbitmqctl join_cluster rabbit@rabbitmq-node1  # 若跨宿主机，需用 IP:25672（如 192.168.1.100:25672）

rabbitmqctl start_app

# 查询集群状态
rabbitmqctl cluster_status



```



