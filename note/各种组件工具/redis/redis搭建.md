# 主从搭建

## 配置

在 Redis 中配置主从复制关系时，你可以选择在配置文件中配置 `replicaof`（在 Redis 5.0 之前为 `slaveof`），或者在客户端通过命令行设置 `SLAVEOF` 命令。这两种方法有一些关键的区别：

### 在配置文件中配置 `replicaof`

```plaintext
# redis.conf (从节点)
port 6380
bind 0.0.0.0  # 确保可以接受外部连接

# 指定主节点的IP和端口
replicaof 192.168.1.100 6379

# 如果主节点设置了密码，则需要配置主节点密码
masterauth <master-password>
```

#### 优点

1. **持久性**：该配置是持久的，Redis 服务器每次启动时都会自动读取配置文件并建立主从关系。
2. **简化操作**：适用于需要长期保持主从关系的场景，减少了每次重启后手动配置的步骤。

#### 缺点

1. **灵活性较低**：如果需要临时更改主从关系，修改配置文件后需要重启 Redis 服务器，可能导致服务中断。

### 在客户端使用 `SLAVEOF` 命令

```sh
redis-cli -p 6380
> SLAVEOF 192.168.3.102 6380
```

#### 优点

1. **灵活性高**：可以动态调整主从关系，无需重启 Redis 服务器，适用于需要临时切换或调整主从关系的情况。
2. **即时生效**：命令执行后立即生效，无需重启服务器。

#### 缺点

1. **非持久性**：该配置在 Redis 服务器重启后会失效，需要在每次启动后重新设置。如果需要持久性，还需要额外处理，例如通过启动脚本自动执行 `SLAVEOF` 命令。
2. **手动操作**：每次修改主从关系时需要手动执行命令，适用于较小规模或变化频繁的场景。

### 适用场景总结

- **配置文件方式**适用于长期稳定的主从复制关系，尤其在生产环境中，为了减少手动操作和配置的出错概率，通常会选择这种方式。
- **命令行方式**适用于需要临时调整主从关系的场景，例如在故障转移或测试环境中，可以更灵活地进行操作。

### 实际操作建议

- 在生产环境中，通常会将主从复制配置写入配置文件以确保配置的持久性和稳定性。
- 在开发、测试或需要临时调整主从关系的情况下，可以通过 `SLAVEOF` 命令进行灵活配置。

# 哨兵搭建

## 配置

```shell
#主要更改下面两个配置
sentinel monitor test 192.168.80.130 6379 2
sentinel auth-pass test 123456

#这里的主机别名是可以自定义的，只需要保证这几个哨兵之间使用相同的名字就可以了。如果使用的名字不同可能会出现问题。

#然后启动程序 
[root@node-129 src]# ./redis-sentinel ../sentinel.conf
```

## 启动脚本

```shell

#!/bin/sh
#Configurations injected by install_server below....

EXEC=/root/redis-6.2.14/src/redis-server
CLIEXEC=/root/redis-6.2.14/src/redis-cli
PIDFILE=/var/run/redis_6379.pid
CONF="/root/redis-6.2.14/redis.conf"
# 如果是哨兵模式加上下面这个
# SENTINELCONF="/root/redis-6.2.14/sentinel.conf"
REDISPORT="6379"

# 接收脚本的第一个参数来决定执行什么命令
case "$1" in
    start)
        # 判断pid文件是否存在
        if [ -f $PIDFILE ]
        then
            echo "$PIDFILE exists, process is already running or crashed"
        else
            echo "Starting Redis server..."
            $EXEC $CONF
            # 哨兵模式加上下面这个
            # $EXEC $SENTINELCONF --sentinel
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
            echo "$PIDFILE does not exist, process is not running"
        else
            PID=$(cat $PIDFILE)
            echo "Stopping ..."
            $CLIEXEC -p $REDISPORT shutdown
            # -x用于检查路径是否存在且可执行
            while [ -x /proc/${PID} ]
            do
                echo "Waiting for Redis to shutdown ..."
                sleep 1
            done
            echo "Redis stopped"
        fi
        ;;
    status)
        PID=$(cat $PIDFILE)
        if [ ! -x /proc/${PID} ]
        then
            echo 'Redis is not running'
        else
            echo "Redis is running ($PID)"
        fi
        ;;
    restart)
        $0 stop
        $0 start
        ;;
    *)
        echo "Please use start, stop, restart or status as first argument"
        ;;
esac

```





# Dcoker

 

[Docker 安装 Redis 容器 (完整详细版) - 鹏星 - 博客园](https://www.cnblogs.com/lzp110119/p/17869310.html)



# Dokcer 主从搭建



redis-replication-config.sh

方便创建配置文件的 shell 脚本

```sh
# 主节点和从节点是不一样的
mkdir -p /root/redis/server/node-6386/conf
touch /root/redis/server/node-6386/conf/redis.conf
cat  << EOF > /root/redis/server/node-6386/conf/redis.conf
port 6386
requirepass 123456
bind 0.0.0.0
protected-mode no
daemonize no
appendonly yes

# 设定连接主节点所使用的密码, 如果后续这个节点变成从节点后方便连接到主节点,所以保持密码一直很关键
masterauth "123456"
EOF

for port in $(seq 6387 6388);
do
mkdir -p /root/redis/server/node-${port}/conf
touch /root/redis/server/node-${port}/conf/redis.conf
cat  << EOF > /root/redis/server/node-${port}/conf/redis.conf
port ${port}
requirepass 123456
bind 0.0.0.0
protected-mode no
daemonize no
appendonly yes

# 配置master节点信息
# 格式：
#slaveof <masterip> <masterport>
# 此处masterip所指定的redis-server-master是运行master节点的容器名
# Docker容器间可以使用容器名代替实际的IP地址来通信
slaveof 192.168.3.102 6386

# 设定连接主节点所使用的密码
masterauth "123456"

EOF
done
```



redis-replication-start.sh

快速启动这三个服务的脚本 

```sh
for port in $(seq 6386 6388); \
do \
   docker run -it -d -p ${port}:${port} -p 1${port}:1${port} \
  --privileged=true -v /root/redis/server/node-${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf \
  --privileged=true -v /root/redis/server/node-${port}/data:/data \
  --restart=unless-stopped --name redis-${port} --net host \
  --sysctl net.core.somaxconn=1024 redis redis-server /usr/local/etc/redis/redis.conf
done
```



```sh
./redis-replication-config.sh
./redis-replication-start.sh
```





# Docker 哨兵搭建

[Docker部署Redis哨兵模式 - 小白一只726 - 博客园](https://www.cnblogs.com/alinainai/p/14086960.html)

这里继续以 192.168.3.102:6383 作为主节点  再创建 6384 6385 作为两个从节点并启动哨兵



redis-sentinel-config.sh

创建哨兵配置文件的shell脚本

```sh
for port in $(seq 26386 26388);
do
mkdir -p /root/redis/sentinel/node-${port}
touch /root/redis/sentinel/node-${port}/sentinel.conf
cat  << EOF > /root/redis/sentinel/node-${port}/sentinel.conf
# bind 127.0.0.1
bind 0.0.0.0
# 哨兵的端口号
# 因为各个哨兵节点会运行在单独的Docker容器中
# 所以无需担心端口重复使用
# 如果需要在单机
port ${port}

# 设定密码认证
requirepass 123456

# 配置哨兵的监控参数
# 格式：sentinel monitor <master-name> <ip> <redis-port> <quorum>
# master-name是为这个被监控的master起的名字
# ip是被监控的master的IP或主机名。因为Docker容器之间可以使用容器名访问，所以这里写master节点的容器名
# redis-port是被监控节点所监听的端口号
# quorom设定了当几个哨兵判定这个节点失效后，才认为这个节点真的失效了
sentinel monitor local-master 192.168.3.102 6386 2

# 连接主节点的密码
# 格式：sentinel auth-pass <master-name> <password>
sentinel auth-pass local-master 123456

# master在连续多长时间无法响应PING指令后，就会主观判定节点下线，默认是30秒
# 格式：sentinel down-after-milliseconds <master-name> <milliseconds>
sentinel down-after-milliseconds local-master 30000
EOF
done
```

启动shell脚本 redis-sentinel-start.sh

```sh
for port in $(seq 26386 26388); \
do \
   docker run -it -d -p 2${port}:2${port}  \
  --privileged=true -v /root/redis/sentinel/node-{port}/sentinel.conf:/usr/local/etc/redis/redis-sentinel.conf \
  --privileged=true -v /root/redis/sentinel/node-${port}/data:/data \
  --restart=unless-stopped --name redis-sentinel-${port} --net host \
  --sysctl net.core.somaxconn=1024 redis redis-sentinel /usr/local/etc/redis/redis-sentinel.conf
done
```



```
./redis-sentinel-config.sh
./redis-sentinel-start.sh
```



配置完成后：

1. 1主2从的redis配置
2. 哨兵集群的配置能够帮助主节点失效后的自动选举，以及自动切换从节点复制



# Docker Compose 搭建主从哨兵



redis服务器本身的 docker-compose.yml 文件

```yaml
---

version: '3'

services:
  # 主节点的容器
  redis-server-master:
    image: redis
    container_name: redis-6380
    restart: unless-stopped
    # 为了规避Docker中端口映射可能带来的问题
    # 这里选择使用host网络
    network_mode: myredis
    # 指定时区，保证容器内时间正确
    environment:
      TZ: "Asia/Shanghai"
    volumes:
      # 映射配置文件和数据目录
      - /root/redis/server/node-6380/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /root/redis/server/node-6380/data:/data
    sysctls:
      # 必要的内核参数
      net.core.somaxconn: '511'
    command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
    ports:
      - "6380:6380"
      # 因为这个节点配置了集群,所以要开发 16380 这个端口
      - "16380:16380"
    privileged: true
  # 从节点1的容器
  redis-server-slave-1:
    image: redis
    container_name: redis-server-slave-1
    restart: unless-stopped
    network_mode: myredis
    depends_on:
      - redis-server-master
    environment:
      TZ: "Asia/Shanghai"
    volumes:
      - /root/redis/server/node-6383/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /root/redis/server/node-6383/data:/data
    sysctls:
      net.core.somaxconn: '511'
    command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
    ports:
      - "6383:6383"
    privileged: true
      # 从节点2的容器
  redis-server-slave-2:
    image: redis
    container_name: redis-server-slave-2
    restart: unless-stopped
    network_mode: myredis
    depends_on:
      - redis-server-master
    environment:
      TZ: "Asia/Shanghai"
    volumes:
      - /root/redis/server/node-6384/conf/redis.conf:/usr/local/etc/redis/redis.conf
      - /root/redis/server/node-6384/data:/data
    sysctls:
      net.core.somaxconn: '511'
    command: [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
    ports:
      - "6384:6384"
    privileged: true
```



为了编排容器的启动顺序还需要引入 docker-compose ，下面是一个sentinel 的 docker-compose.yml的配置

```yml
---

version: '3'

services:
  redis-sentinel-1:
    image: redis
    container_name: redis-sentinel-1
    restart: unless-stopped
    # 为了规避Docker中端口映射可能带来的问题
    # 这里选择使用host网络
    network_mode: myredis
    volumes:
      - /root/redis/sentinel/node-26380/sentinel.conf:/usr/local/etc/redis/redis-sentinel.conf
    # 指定时区，保证容器内时间正确
    environment:
      TZ: "Asia/Shanghai"
    sysctls:
      net.core.somaxconn: '511'
    command: ["redis-sentinel", "/usr/local/etc/redis/redis-sentinel.conf"]
    ports:
      - "26380:26380"
    privileged: true
  redis-sentinel-2:
    image: redis
    container_name: redis-sentinel-2
    restart: unless-stopped
    network_mode: myredis
    volumes:
      - /root/redis/sentinel/node-26383/sentinel.conf:/usr/local/etc/redis/redis-sentinel.conf
    environment:
      TZ: "Asia/Shanghai"
    sysctls:
      net.core.somaxconn: '511'
    command: ["redis-sentinel", "/usr/local/etc/redis/redis-sentinel.conf"]
    ports:
      - "26383:26383"
    privileged: true
  redis-sentinel-3:
    image: redis
    container_name: redis-sentinel-3
    restart: unless-stopped
    network_mode: myredis
    volumes:
      - /root/redis/sentinel/node-26384/sentinel.conf:/usr/local/etc/redis/redis-sentinel.conf
    environment:
      TZ: "Asia/Shanghai"
    sysctls:
      net.core.somaxconn: '511'
    command: ["redis-sentinel", "/usr/local/etc/redis/redis-sentinel.conf"]
    ports:
      - "26384:26384"
    privileged: true

```



先启动三个服务，我这里在搭建docker集群的时候6380服务已经启动了，所以这里有点麻烦。

我把6380的容器给删掉了，因为配置文件和数据文件都是外挂的，所以没啥问题，删掉以后重新通过 docker compose 创建了一个新的容器，端口号没变。

```sh
./redis-cluster-config-sh.sh
./redis-slave-sentinel-redis-config.sh
./redis-slave-sentinel-client-config.sh



./redis-cluster-docker-start.sh

redis-cli -a 123456 --cluster create 192.168.3.102:6380 192.168.3.102:6381 192.168.3.102:6382 --cluster-replicas 1

# 启动三个主从关系的redis
docker compose -f redis-sentinel-redis-docker-compose.yml up -d
# 如果要关闭docker compose
docker compose -f redis-sentinel-redis-docker-compose.yml stop

# 启动三个哨兵
docker compose -f redis-sentinel-client-docker-compose.yml up -d
docker compose -f redis-sentinel-client-docker-compose.yml stop
```



这里有个问题：

集群的配置已经配置好了ip加端口，但是如果某个主节点具有1主2从的主从复制结构，恰巧主节点挂了，从节点选举成为了新的主节点，然后原来的主节点再重启后变成了从节点，这个主从复制架构的主节点就变了，集群内如何检测到这个新的主节点呢？





# Docker 集群搭建

[史上最详细Docker搭建Redis Cluster集群环境 值得收藏 每步都有图，不用担心学不会_redis cluster 搭建 docker-CSDN博客](https://blog.csdn.net/weixin_45821811/article/details/119421774)



redis-cluster-config.sh

创建集群配置文件的shell脚本

```sh
for port in $(seq 6380 6385);
do
mkdir -p /root/redis/cluster/node-${port}/conf
touch /root/redis/cluster/node-${port}/conf/redis.conf
cat  << EOF > /root/redis/cluster/node-${port}/conf/redis.conf
port ${port}
requirepass 123456
bind 0.0.0.0
protected-mode no
daemonize no
# 开启aof持久化
appendonly yes

# 启用 Redis 集群模式（Cluster Mode）。设为 yes后，Redis 会以集群模式启动，支持自动分片、故障转移等功能。
cluster-enabled yes
# 指定集群节点元数据的存储文件（自动生成和维护）。文件中记录各节点的 ID、IP、端口、状态、槽分配等信息。
cluster-config-file nodes.conf
# 设置集群节点的超时时间（单位：毫秒）。若某个节点超过此时间未响应（如心跳包丢失），会被标记为 FAIL，触发故障转移（主节点降级，从节点晋升）。
cluster-node-timeout 5000
# 显式声明当前节点的对外可访问 IP。集群节点间通信时，会使用此 IP 而非 Redis 内部监听的 IP（如容器或内网 IP）。
cluster-announce-ip 192.168.3.102
# 声明当前节点对外暴露的服务端口（与 port一致）。集群节点间通过此端口进行 Gossip 协议通信（交换状态信息）。
cluster-announce-port ${port}
# 声明集群总线的端口（用于节点间实时通信）。Redis 集群通过 port + 10000的端口（如 6380 → 16380）进行 Gossip 协议的高效通信（二进制协议，低延迟）。
cluster-announce-bus-port 1${port}

# 设定连接主节点所使用的密码
masterauth "123456"
EOF
done

```



redis-cluster-start.sh

启动集群docker的shell脚本

```sh
for port in $(seq 6380 6385); \
do \
   docker run -it -d -p ${port}:${port} -p 1${port}:1${port} \
  --privileged=true -v /root/redis/cluster/node-${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf \
  --privileged=true -v /root/redis/cluster/node-${port}/data:/data \
  --restart=unless-stopped --name redis-${port} --net redis-cluster \
  --sysctl net.core.somaxconn=1024 redis redis-server /usr/local/etc/redis/redis.conf
done

```



```sh
./redis-cluster-config.sh
./redis-cluster-start.sh
```

**创建集群**

进入任意一个节点：docker exec -it 容器id sh

redis-cli -a 123456 -p 6380 --cluster create 192.168.3.102:6380 192.168.3.102:6381 192.168.3.102:6382 192.168.3.102:6383 192.168.3.102:6384 192.168.3.102:6385 --cluster-replicas 1

这个命令会执行：
1️⃣ 把这 6 个节点组成一个 Cluster。

2️⃣ 自动把它们分配成：

- 3 个主节点（负责所有 slots）
- 每个主节点配 1 个从节点（从节点只复制，不分配 slots）

3️⃣ 所以最终结果是：

- 3 主 3 从
- 主节点：6380、6381、6382（假设这 3 个被选为主）
- 从节点：6383、6384、6385（分别挂到主节点下）
- slots 会平均分配给 3 个主节点。

4️⃣ 所有节点的 `nodes.conf` 文件会被更新，记录各自的 ID、角色、slots、从属关系。

5️⃣ 集群内部会自动启动 Gossip 协议，用来交换拓扑信息和检测节点状态。



