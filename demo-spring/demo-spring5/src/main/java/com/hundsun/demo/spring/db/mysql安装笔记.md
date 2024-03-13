    my.cnf 文件随便找一个就行
    主要修改
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
Starting MySQL./home/mysql/mysql-8.0.25/bin/mysqld_safe: line 653: /var/log/mariadb/mariadb.log: No such file or directory  
2023-11-07T02:53:32.292578Z mysqld_safe error: log-error set to '/var/log/mariadb/mariadb.log', however file don't exists. Create writable for user '
mysql'.  
/home/mysql/mysql-8.0.25/bin/mysqld_safe: line 144: /var/log/mariadb/mariadb.log: No such file or directory  
ERROR! The server quit without updating PID file (/home/mysql/mysql-8.0.25/run/mysqld.pid).

A:  
这个就是 mysqld_safe 这个程序的启动参数没有指定配置文件, 导致log文件的路径配置采用的是默认的, 而路径上又没有这个文件夹或者权限导致  
修改 mysql 启动脚本,指定配置文件 $bindir/mysqld_safe --defaults-file="/home/mysql/mysql-8.0.25/conf/my.cnf" $other_args >/dev/null &

Q:   
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/tmp/mysql.sock' (111)

A:  
方法1: ./mysql -uroot -h127.0.0.1 -p123456 (-h 用于指定要连接的 MySQL 服务器的主机名或 IP 地址。)  
方法2: ./mysql -S /home/mysql/mysql-8.0.25/run/mysql.sock -uroot -p123456 (-S 用于指定 MySQL 服务器的 socket 路径（或套接字文件路径），而不是使用默认路径 /tmp/mysql.sock。)