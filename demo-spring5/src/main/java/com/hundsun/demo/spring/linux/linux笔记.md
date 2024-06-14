    useradd mysql -d /home/mysql -m;
    echo "mysql" | passwd --stdin mysql;

    chkconfig --list 开机启动列表