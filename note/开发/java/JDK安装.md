https://www.cnblogs.com/antLaddie/p/17599359.html

## 彻底卸载Java环境

```shell
# RPM方式卸载Java环境：
# rpm查询java安装包名称（注：rpm -qa 列举出所有RPM安装的包）
# 执行查询命令
rpm -qa | grep 'java\|jdk\|gcj\|jre'
# 卸载查询出来的所有安装包名称（注：rpm -e --nodeps 是RPM卸载命令）
# 执行卸载命令（查询出来什么就卸载什么）
rpm -e --nodeps jdk-1.8-1.8.0_381-9.x86_64
# 补充
# 前面可以查询安装包安装到系统的文件位置（rpm -ql 安装包名称）
# 如：rpm -ql jdk-1.8-1.8.0_381-9.x86_64

# 快速卸载所有包含 'java\|jdk\|gcj\|jre' 的软件
rpm -qa | grep 'java\|jdk\|gcj\|jre' | xargs sudo rpm -e --nodeps

# yum方式卸载Java环境：
# yum查询Java安装的环境信息（注：yum list installed 列举所有安装的服务）
# 执行命令
yum list installed | grep 'java\|jdk\|gcj\|jre'

# 卸载查询出来的所有安装信息（注：yum -y remove 是yum卸载命令）
# 执行卸载命令（查询出来什么就卸载什么）
yum -y remove jdk-1.8.x86_64
# 使用yum批量卸载所有包含 jdk 的软件
# sudo yum remove \*jdk\* \*java\*

# tar.gz方式安装后的卸载：
# 删除之前的解压文件位置（如下是我的文件位置）
rm -rf /usr/local/jdk1.8.0_381/
# 剔除之前配置的 /etc/profile 下的配置信息，如下示例：
## 剔除这些配置信息
export JAVA_HOME=/usr/local/jdk1.8.0_381
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib
# 
source /etc/profile
```


## 安装 Java 运行环境

### 使用 rpm 安装 JDK
```shell
# 将下载好的rpm包上传到服务器
# -i安装验证软件包 -v显示详细信息 -h显示安装进度条 
rpm -ivh jdk-8u421-linux-x64.rpm
# 查看版本, 命令不报错则安装成功
java -version
# 查看java的安装目录 输出 /usr/bin/java
which java 
```

### 使用 yum 安装 JDK 
```shell
# 如果已经使用rpm安装了jdk之后,yum这里同样能看见
# yum是一个高级的包管理工具,在后台依旧调用了rpm,同时它处理软件包的依赖关系和冲突
# 我们这里可以卸载一下之前安装的jdk
yum list installed | grep 'java\|jdk\|gcj\|jre'
# grep -i忽略大消息 --color用于高亮显示匹配的关键字
# 但是yum搜索的结果只有openjdk的,我们这里使用的是oraclejdk
yum search java | grep -i --color jdk
yum install -y java-1.8.0-openjdk-src.x86_64
# openjdk这个不自带开发工具, 如果要使用调试工具, 还需要额外下载一个软件包
yum install java-1.8.0-openjdk-devel.x86_64 -y


```

### 使用 tar.gz 手动安装
```shell
# 解压 tar.gz 压缩包
tar -zxvf jdk-8u381-linux-x64.tar.gz
# 剪贴到/usr/local目录下（注：此目录通常编译或安装不是直接来自官方仓库或软件包管理器的软件包） 可做可不做
# 配置Java环境变量
```

## 配置 Java 环境变量

```shell
# 修改配置环境
vim /etc/profile
# 在文件的最后加上这三行
# export JAVA_HOME=/usr/local/jdk1.8.0_381    # 这里设置解压的Java目录文件
# export PATH=$JAVA_HOME/bin:$PATH
# export CLASSPATH=.:$JAVA_HOME/lib

# 使配置立马生效
source /etc/profile

# 检查Java是否安装成功
java -version
```