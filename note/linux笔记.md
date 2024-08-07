    useradd mysql -d /home/mysql -m;
    echo "es" | passwd --stdin es;

    chkconfig --list 开机启动列表


给es用户授权，包括es文件下的所有文件和文件夹，授权给es组下的es用户
chown -R es:es es