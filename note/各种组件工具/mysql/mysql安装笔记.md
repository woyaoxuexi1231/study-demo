# 源码安装

## 配置文件更改

my.cnf 文件随便找一个就行
```cnf
[mysqld]
datadir=/home/mysql/mysql-8.0.25/data
basedir=/home/mysql/mysql-8.0.25

user=mysql
port=3306
server-id=1
character-set-server=utf-8
explicit_defaults_for_timestamp=true
log-error=/home/mysql/mysql-8.0.25/log/error.log
pid-file=/home/mysql/mysql-8.0.25/run/mysqld.pid
max_connections=4000

# see configurations
# expire_logs_days=${expire_logs_days}
binlog_expire_logs_seconds=604800
innodb_lock_wait_timeout=50
innodb_print_all_deadlocks=OFF
interactive_timeout=28800
log-queries-not-using-indexes = OFF
long_query_time=1
max_binlog_size=128M
max_connect_errors=1000
slow-query-log = OFF
table_definition_cache=16384
table_open_cache=16384
transaction-isolation = READ-COMMITTED
wait_timeout=28800


skip-name-resolve
log_bin_trust_function_creators=true
innodb_strict_mode=true
innodb_directories=/home/mysql/mysql-8.0.25/data
character-set-server=utf8mb4
collation_server=utf8mb4_bin
autocommit=1
sync_relay_log=1
innodb_autoinc_lock_mode = 2
back-log=500
innodb-status-file=TRUE
default-storage-engine=InnoDB
innodb_fast_shutdown=0
binlog_format=row
log-bin=/home/mysql/mysql-8.0.25/binlog/mysql-bin
log_timestamps=System
binlog_checksum=NONE
binlog_row_image=full
binlog_cache_size = 8M
# max_binlog_cache_size = 128M
innodb-log-buffer-size=8M
innodb_log_file_size=1G
innodb_log_files_in_group=3
default-tmp-storage-engine=MEMORY
innodb-autoextend-increment=100
innodb-file-per-table=true
innodb_rollback_on_timeout=true
open-files-limit=65535
innodb_open_files=65535
log-output=FILE
slow-query-log
slow-query-log-file=/home/mysql/mysql-8.0.25/log/slow.log			
group_concat_max_len=102400
innodb_purge_threads=4
join_buffer_size=134217728
read_buffer_size=8388608
read_rnd_buffer_size=8388608
sort_buffer_size=2097152
transaction_isolation=READ-COMMITTED
innodb_flush_method=O_DIRECT
innodb_flush_log_at_trx_commit=1
sync_binlog=1
create_admin_listener_thread=ON

[mysqld]
socket=/home/mysql/mysql-8.0.25/run/mysql.sock

[mysqld_safe]
log-error=/home/mysql/mysql-8.0.25/log/error.log
socket=/home/mysql/mysql-8.0.25/run/mysql.sock

[client]
socket=/home/mysql/mysql-8.0.25/run/mysql.sock


```

**修改 socket文件的路径, 配置文件中类似 run,log 这样的文件夹要提前建好**

mysql启动脚本, 可以直接 cp support-file下面的就行

* 主要, 要修改启动脚本里的
  datadir, basedir, 和指定启动参数的配置文件

Q:
Starting MySQL./home/mysql/mysql-8.0.25/bin/mysqld_safe: line 653: /var/log/mariadb/mariadb.log: No such file or
directory
2023-11-07T02:53:32.292578Z mysqld_safe error: log-error set to '/var/log/mariadb/mariadb.log', however file don't
exists. Create writable for user '
mysql'.
/home/mysql/mysql-8.0.25/bin/mysqld_safe: line 144: /var/log/mariadb/mariadb.log: No such file or directory
ERROR! The server quit without updating PID file (/home/mysql/mysql-8.0.25/run/mysqld.pid).

A:
这个就是 mysqld_safe 这个程序的启动参数没有指定配置文件, 导致log文件的路径配置采用的是默认的, 而路径上又没有这个文件夹或者权限导致
修改 mysql 启动脚本,指定配置文件 $bindir/mysqld_safe --defaults-file="/home/mysql/mysql-8.0.25/conf/my.cnf"
$other_args >/dev/null &

Q:
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/tmp/mysql.sock' (111)

A:
方法1: ./mysql -uroot -h127.0.0.1 -p123456 (-h 用于指定要连接的 MySQL 服务器的主机名或 IP 地址。)
方法2: ./mysql -S /home/mysql/mysql-8.0.25/run/mysql.sock -uroot -p123456 (-S 用于指定 MySQL 服务器的 socket
路径（或套接字文件路径），而不是使用默认路径 /tmp/mysql.sock。)

## 主从复制搭建

配置复制参数：
主数据库和从数据库的配置文件都添加一个参数: server-id=xxx
两者不一样即可,改完之后重启

从数据执行操作:

通过命令的形式搭建一个主从复制的过程如下：

1. 在主节点执行这个命令获取主节点的binlog信息

   > show master status;

2. 开启从库的复制功能

   > CHANGE MASTER TO MASTER_HOST='192.168.80.128',MASTER_USER='root',MASTER_PASSWORD='123456'
   > ,MASTER_LOG_FILE='mysql-bin.000027',MASTER_LOG_POS=152;
   > start slave;

3. 可能会出现各种问题，可以清理掉之前的配置，执行以下命令

   > stop slave;
   > reset slave all;

**可能出现的问题:**

1. 2024-05-07T23:27:51.325572+08:00 9 [ERROR] [MY-013117] [Repl] Slave I/O for channel '': Fatal error: The slave I/O
   thread stops because master and slave have equal MySQL server UUIDs; these UUIDs must be different for replication to
   work. Error_code: MY-013117
   删除从数据库的 auto.cnf 文件,这个文件会生成一个服务id,两个数据库的服务id不能相同
2. 从数据库并不会直接复制现有数据库的schema,所以进行主从复制的前置操作:我们需要对从库进行初始化操作(导入主库的数据)
3. 我操作从库之后,让主从的数据不一致,再操作主库同一张表之后Slave_SOL_Running变为NO,由于复制SQL语句的时候出现错误导致.
   我清理了之前的配置,执行stop slave和reset slave
   all,不一致的数据依然存在.当我清理掉不一致的数据之后,再次尝试复制却发现丢掉的数据没有了.所以我只能手工维护之前丢掉的数据了为了解决这个问题,我们需要限制从库的写能力:

    - 直接设置从库的只读模式,在my.cnf配置文件中设置read_only=1
    - 设置用户的权限,仅开放一些拥有读权限的用户
      CREATE USER 'readonly_user'@'%' IDENTIFIED BY 'password';
      GRANT SELECT ON *.* TO 'readonly_user'@'%';
    - 应用本身控制 或者 使用数据库中间件或代理服务器如ProxySQL、MaxScale等，可以帮助实现读写分离，自动将写请求定向到主数据库，而读请求则可以定向到从数据库
4. Could not find first log file name in binary log index file
   这个应该是日志同步的日志文件写错了,重新改一下 change master to 语句的参数



# Docker安装

[Docker部署MySQL8.1版本，并挂载存储，日志，配置文件_docker安装mysql8并且挂载数据日志配置目录-CSDN博客](https://blog.csdn.net/Fly_wd/article/details/133869657)



创建持久化存储，日志，配置文件目录

```bash
# 创建存储目录，日志目录，配置文件目录
mkdir -p /root/mysql/{conf,data,log,binlog,mysql-files} 
```



创建配置文件

```sh
touch /root/mysql/conf/my.cnf
```



```cnf
[client]
#设置客户端默认字符集utf8mb4
default-character-set=utf8mb4
[mysql]
#设置服务器默认字符集为utf8mb4
default-character-set=utf8mb4

[mysqld]
# basedir 参数指定 MySQL 安装目录的路径。这个目录包含 MySQL 的二进制文件、库文件、配置文件等。
# 默认位置 通常在 /usr 或 /usr/local/mysql 下。
# basedir=/home/mysql
# datadir 参数指定 MySQL 数据库文件存储的目录。这个目录包含所有的数据库文件、表空间文件、二进制日志文件等。
# Linux：通常在 /var/lib/mysql 下。
# datadir=/home/mysql/data
# slow-query-log-file=/home/mysql/log/slow.log
# 错误日志配置写入文件后，就不会在控制台输出了，这样docker logs就看不到了, 默认是 stderr
# log-error=/var/log/mysql/error.log
# 在 MySQL 配置文件中，pid-file 参数指定了 MySQL 服务器进程的 PID（Process ID）文件的位置。PID 文件包含了 MySQL 服务器进程的 ID，用于管理和跟踪 MySQL 服务器的进程状态，例如启动、停止和重启操作。
# 在一些 Linux 发行版中，默认位置是 /var/run/mysqld/mysqld.pid
# pid-file=/home/mysql/run/mysqld.pid
# innodb_directories 参数允许指定多个目录，用于 InnoDB 表空间文件的存储位置。这个参数可以用于将表空间文件存储在不同的磁盘或分区上，以提高性能和管理效率。
# innodb_directories 没有默认值，需要在 MySQL 配置文件中显式设置。如果没有设置此参数，InnoDB 表空间文件将存储在 MySQL 数据目录中。
# innodb_directories=/home/mysql/data

# log-bin 参数用于启用 MySQL 的二进制日志功能，并指定二进制日志文件的基本文件名和存储位置。二进制日志记录了所有更改数据库的操作，对于复制（replication）和数据恢复非常重要。
# 如果没有显式设置 log-bin 参数，二进制日志文件将存储在 MySQL 数据目录中，并以主机名为文件名前缀。
# log-bin=/home/mysql/binlog/mysql-bin

user=mysql
port=3306
server-id=1
character-set-server=utf-8
explicit_defaults_for_timestamp=true
max_connections=4000

# see configurations
# expire_logs_days=${expire_logs_days}
binlog_expire_logs_seconds=604800
innodb_lock_wait_timeout=50
innodb_print_all_deadlocks=OFF
interactive_timeout=28800
log-queries-not-using-indexes = OFF
long_query_time=1
max_binlog_size=128M
max_connect_errors=1000
slow-query-log = OFF
table_definition_cache=16384
table_open_cache=16384
wait_timeout=28800


skip-name-resolve
log_bin_trust_function_creators=true
innodb_strict_mode=true
character-set-server=utf8mb4
collation_server=utf8mb4_bin
autocommit=1
sync_relay_log=1
innodb_autoinc_lock_mode = 2
back-log=500
innodb-status-file=TRUE
default-storage-engine=InnoDB
innodb_fast_shutdown=0
binlog_format=row
log_timestamps=System
binlog_checksum=NONE
binlog_row_image=full
binlog_cache_size = 8M
# max_binlog_cache_size = 128M
innodb-log-buffer-size=8M
innodb_log_file_size=1G
innodb_log_files_in_group=3
default-tmp-storage-engine=MEMORY
innodb-autoextend-increment=100
innodb-file-per-table=true
innodb_rollback_on_timeout=true
open-files-limit=65535
innodb_open_files=65535
log-output=FILE
slow-query-log
group_concat_max_len=102400
innodb_purge_threads=4
join_buffer_size=134217728
read_buffer_size=8388608
read_rnd_buffer_size=8388608
sort_buffer_size=2097152
transaction_isolation=REPEATABLE-READ
innodb_flush_method=O_DIRECT
innodb_flush_log_at_trx_commit=1
sync_binlog=1
create_admin_listener_thread=ON
```



解决权限问题

```bash
chmod 777 /root/mysql/data/ /root/mysql/log

chmod 644 /root/mysql/conf/my.cnf

# my.cnf配置文件必须设置644权限，设置777在登录mysql时报警告配置文件无法生效。
```



启动 Mysql 镜像

解析：

-p：主机端口：容器端口。

-d：启动的镜像名称。

--restart=always：设置docker启动时，容器跟随自启。

--name：设置容器名称。

--privileged=true：赋予容器权限修改宿主文件权利。

-v /root/mysql/log:/var/log/mysql：挂载容器日志到宿主，方便查看日志。

-v /root/mysql/conf/my.cnf:/etc/mysql/my.cnf:挂载容器配置文件到宿主，方便修改配置文件。

-v /root/mysql/data:/var/lib/mysql：挂载容器存储文件到宿主，避免因不小心卸载容器或者容器损坏导致数据丢失不可找回风险。

-v /root/mysql/mysql-files:/var/lib/mysql-files 

-e MYSQL_ROOT_PASSWORD=123456：设置MySQL的root用户的密码。

--lower-case-table-names=1：用于指定表名是否进行大小写不敏感的处理。

```bash
docker run -p 3306:3306 --restart=always --name mysql \
-e MYSQL_ROOT_PASSWORD=123456 \
-e TZ=Asia/Shanghai \
--privileged=true \
-v /root/mysql/log:/var/log/mysql \
-v /root/mysql/data:/var/lib/mysql \
-v /root/mysql/conf/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql/mysql-files:/var/lib/mysql-files \
-d mysql

# 这里报错了
bfcd0a4b57f4c58de7b4ea3848f47bcd312787d126aabb5b26ddb60e8a5efe50
docker: Error response from daemon: driver failed programming external connectivity on endpoint mysql (c28ab5b3311970c604eee8cba5962c4f85581a4736fbfef77675128c885c2d99):  (iptables failed: iptables --wait -t nat -A DOCKER -p tcp -d 0/0 --dport 3306 -j DNAT --to-destination 172.17.0.2:3306 ! -i docker0: iptables: No chain/target/match by that name.
 (exit status 1)).
 
# 这个错误通常是由于 iptables 链或规则不存在导致的
sudo iptables -F 
sudo systemctl restart docker
```



进入容器配置MySQL可以被远程地址访问

```bash
docker exec -it mysql bash

mysql -uroot -p123456

use mysql;
update user set host = '%' where user = 'root';
flush privileges;
```

