# 用户操作

```shell
## 用户信息本身的操作
# sudo(superuser do) 这个命令可以让普通用户能够以超级用户的权限来执行指定的命令
# 创建用户 -m创建用户主目录 -g指定用户所属的主要组 -G指定用户所属的次要组 -s指定用户登录时使用shell
useradd username
# 设置或修改用户密码, passwd指定一个用户后,系统会进行提示输入新密码并确认
passwd username
# 删除用户 -r参数可以用于同时删除出用户的主目录和邮件目录
userdel username
# 修改用户信息 -c修改用户注释 -d修改用户的工作目录 -g同新增 -G同新增 -s同新增 -l修改用户的登录名 -a(append)附加,通常于G一起使用
usermod [options] username
# 查看用户信息
id username
# 查看用户列表
cat /etc/passwd
# root:x:0:0:root:/root:/bin/bash
# root用户  x密码占位符,x表示密码存放在/etc/shadow文件中 0:0 用户id0,组id0, 紧接着root是用户的注释, /root用户的工作目录 /bin/bash 使用bash作为shell


## 组相关操作
# 添加用户到组
usermod -aG groupname username
# 查看组列表
cat /etc/group
# 添加组
groupadd groupname
# 删除组
gourpdel groupname
# 修改组信息
groupmod -n newgroupname oldgroupname


## 
# 创建一个 mysql用户, -d指定mysql的主目录为/home/mysql  -m在创建用户的同时创建这个主目录
useradd mysql -d /home/mysql -m;
# echo "es" 将字符串输出到标准输出,  |管道符,把echo命令的输出作为输入传递给后面的命令, --stdin允许从标准输入读取密码,用于自动化脚本
# 这是一个passwd命令本身支持的操作,不代表其他命令支持
echo "es" | passwd --stdin es;
```

# 虚拟机宕机

![img.png](img.png)

当VMware虚拟机遭遇强制下电后，重启可能会出现系统文件错误。
[参考文章](https://blog.csdn.net/zhanremo3062/article/details/113842682)

```shell
# 修复
xfs_repair /dev/mapper/centos-root
# 退出即可
exit
```

# 开机服务

chkconfig --list 开机启动列表

# free 命令

# tar 命令

```shell
# -c --create 创建新的存档文件
# -x --extract 从存档中提取文件
# -C --directory 指定提取文件的目标目录
# -t --list 列出存档文件中的内容
# -z --gzip 使用gzip进行压缩或者解压缩
# -j --bzip2 使用bzip2进行压缩或者解压缩
# -J --xz 使用xz进行压缩或者解压缩

# -v --verbose 显示详细信息
# -f --file 指定存档文件名

# -x提取xxx.tar.gz文件 z使用gzip方式解压 v显示详细信息 f指定压缩文件 -C到 xxx目录下 
tar -xzvf xxx.tar.gz -C xxx
```

