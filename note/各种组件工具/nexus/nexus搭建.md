## 下载安装包

[官方提供的下载Download](https://help.sonatype.com/en/download.html)

这里下载UNIX版本的压缩包

```shell
# 解压,根据jdk版本下载对应的包
tar -xzvf nexus-3.70.3-01-java8-unix.tar.gz
# ln -s 是一个在 Unix 和类 Unix 操作系统（如 Linux 和 macOS）中用于创建符号链接（symlink）的命令。
# ln -s [源文件或目录] [目标链接名]
ln -s nexus /etc/init.d/nexus
# 启动，start是后台启动，run是前台启动，可以看见日志
./nexus run
# 启动后，访问http://192.168.80.128:8081打开管理页面
# 登录名/密码 admin/默认密码在 /root/nexus/sonatype-work/nexus3/admin.password
# 成功登陆后会提示更换密码
```



## 创建仓库



