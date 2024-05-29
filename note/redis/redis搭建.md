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
> SLAVEOF 192.168.1.100 6379
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

```
主要更改下面两个配置
sentinel monitor test 192.168.80.130 6379 2
sentinel auth-pass test 123456

这里的主机别名是可以自定义的，只需要保证这几个哨兵之间使用相同的名字就可以了。如果使用的名字不同可能会出现问题。

然后启动程序 
[root@node-129 src]# ./redis-sentinel ../sentinel.conf
```

# 启动脚本

``` 

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