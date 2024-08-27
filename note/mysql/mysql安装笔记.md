### 配置文件更改

my.cnf 文件随便找一个就行
主要修改：

[mysqld]
datadir=/home/mysql/mysql-8.0.25/data
basedir=/home/mysql/mysql-8.0.25
socket=/home/mysql/mysql-8.0.25/run/mysql.sock

[mysqld_safe]
log-error=/home/mysql/mysql-8.0.25/log/error.log
socket=/home/mysql/mysql-8.0.25/run/mysql.sock

[client]
socket=/home/mysql/mysql-8.0.25/run/mysql.sock

修改 socket文件的路径, 配置文件中类似 run,log 这样的文件夹要提前建好

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

### 主从复制搭建

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

