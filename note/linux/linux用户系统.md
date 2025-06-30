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
echo "mysql" | passwd --stdin mysql;
```



# 文件系统的权限问题



好问题，我来系统地帮你说明一下：

------

## 🎯 **一、CentOS 中非 root 用户能访问哪些目录**

在 Linux（包括 CentOS）中，文件/目录访问权限由 **权限位（rwx）+ 所有者 + 所属组** + SELinux 决定，核心规则如下：

- **绝大多数系统目录（/bin、/usr、/etc、/var/log 等）对所有用户可读。**
- **非 root 用户不能写入或执行需要特权的目录，除非该目录对普通用户有写权限（如 `/tmp`、用户自己的 `$HOME`）。**
- **`/root` 是 root 用户的家目录，默认只有 root 可以访问，权限一般是 700（`drwx------`）。**
- **`/home/username` 是普通用户自己的家目录，其他用户能否访问取决于家目录权限，默认是 700 或 755（看发行版和配置）。**
- **`/tmp`（临时目录）对所有用户可读写（`drwxrwxrwt`），末尾 `t` 表示 sticky bit，只允许用户删除自己创建的文件。**

✅ **总结：**

- **非 root 用户可以读：** 大多数系统二进制文件和库（`/bin` `/usr/bin` `/lib` `/usr/lib`），配置文件（`/etc`）等。
- **非 root 用户可以写：** 自己的 `$HOME`，`/tmp`，其他对普通用户开放写权限的目录（很少）。
- **`/root` 目录不可访问**（除非提权或修改权限）。

------

## 📂 **二、CentOS 的文件系统架构概览**

| 目录         | 作用             | 存放内容                                                    |
| ------------ | ---------------- | ----------------------------------------------------------- |
| `/`          | 根目录           | 所有文件和目录的起点                                        |
| `/bin`       | 基本用户命令     | 常用的可执行程序，如 `ls`、`cp`、`mv`、`rm` 等              |
| `/sbin`      | 系统管理命令     | 管理命令，通常只有 root 用，如 `shutdown`、`mkfs`           |
| `/usr`       | 用户程序         | 大量的应用程序和库文件，最庞大的子树之一                    |
| `/usr/bin`   | 用户程序         | 大多数用户可执行程序                                        |
| `/usr/sbin`  | 超级用户程序     | 系统级工具                                                  |
| `/usr/local` | 本地安装         | 用户自己安装的软件（源码编译安装常放这里）                  |
| `/lib`       | 系统库           | 基本共享库                                                  |
| `/lib64`     | 64位系统库       | 64 位架构下的库                                             |
| `/etc`       | 配置文件         | 系统和服务配置文件，如 `/etc/passwd` `/etc/ssh/sshd_config` |
| `/var`       | 可变数据         | 日志文件 `/var/log`、邮件 `/var/mail`、数据库、缓存等       |
| `/tmp`       | 临时文件         | 所有人可写临时目录，开机会清理                              |
| `/root`      | root 家目录      | root 用户的家目录                                           |
| `/home`      | 普通用户家目录   | 各个用户的个人数据                                          |
| `/dev`       | 设备文件         | 硬盘、终端、USB 等设备节点                                  |
| `/proc`      | 虚拟文件系统     | 内核和进程信息，实时生成，如 `/proc/cpuinfo`                |
| `/sys`       | 虚拟文件系统     | 内核和硬件信息接口                                          |
| `/boot`      | 启动相关         | 内核和引导加载器文件，如 `vmlinuz` `grub`                   |
| `/mnt`       | 临时挂载点       | 临时挂载外部设备                                            |
| `/media`     | 可移动设备挂载点 | 如 U 盘、光盘自动挂载                                       |
| `/run`       | 运行时数据       | 运行时产生的临时文件，如 PID 文件、Socket                   |
| `/opt`       | 可选软件         | 第三方大软件（如 Oracle、VMware）                           |

------

## 🚫 **`/root` 的特殊性**

- **默认权限是 `700`**：

  ```bash
  drwx------  5 root root 4096 /root
  ```

  这意味着只有 root 用户能读、写、执行（进入）。

- 普通用户尝试访问：

  ```bash
  cd /root
  ```

  会返回：

  ```
  Permission denied
  ```

- 只有提权（`sudo` 或 `su -`）后才能访问。

------

## ✅ **小结**

- **CentOS 是严格权限隔离的：非 root 用户只能访问公开资源。**
- **写操作基本局限于：用户自己的家目录 `/home/username` 和 `/tmp`。**
- **核心配置和系统二进制文件通常对所有人可读（读配置需要），但只能由 root 修改。**
- **`/root` 是 root 私有，普通用户无法访问。**

------

如果需要，我可以帮你：

- 画一个 **Linux 文件系统结构图**
- 或写一个 `tree` 命令演示具体目录结构
- 或生成一些示例 `ls -ld` 输出，帮你理解权限位

要不要给你来一个？🔍✨