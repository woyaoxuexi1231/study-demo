# 问题合集

## 虚拟机宕机

![img.png](linux虚拟机宕机日志截图.png)

![image-20241101003429528](C:\dataz\Project\study-demo\note\images\image-20241101003429528.png)

当VMware虚拟机遭遇强制下电后，重启可能会出现系统文件错误。
[参考文章](https://blog.csdn.net/zhanremo3062/article/details/113842682)

```shell
# 修复
xfs_repair /dev/mapper/centos-root
# 退出即可
exit
```



## Windows换行符问题

```shell
yum -y install dos2unix*
# 使用 dos2unix 格式化文件中的非linux格式的换行符
dos2unix one-more.sh
```



## 通过pid找到服务所在位置

在 Linux 系统中，可以通过进程号（PID）找到服务的位置。以下是一些步骤：

1. **找到进程号**：使用 `ps` 命令查找进程号。例如：
   ```bash
   ps aux | grep <服务名>
   ```
   这将列出所有包含指定服务名的进程及其进程号。

2. **进入 `/proc` 目录**：进程号通常存储在 `/proc` 目录下。例如，如果进程号是 `1234`，可以使用以下命令：
   ```bash
   cd /proc/1234
   ```

3. **查看进程的工作目录**：进入 `/proc/<PID>/cwd` 文件，这个文件的路径就是进程的当前工作目录。例如：
   ```bash
   ls -l /proc/1234/cwd
   ```
   这将显示进程的工作目录。



# 命令合集

## 开机服务

chkconfig --list 开机启动列表

## free 命令

## tar 命令

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



