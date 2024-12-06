# APISIX安装

参考安装教程：[APISIX 安装指南 | Apache APISIX® -- Cloud-Native API Gateway](https://apisix.apache.org/zh/docs/apisix/installation-guide/)

1. 更换yum源

   ```shell
   # 备份一下原始的镜像文件
   mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.back
   # 配置阿里云的镜像
   curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
   # 重新生成缓存
   yum makecache
   ```

2. 安装etcd

   github可以下载源码 https://github.com/etcd-io/etcd

   ```shell
   # 解压
   tar -xzvf etcd-v3.4.34-linux-amd64.tar.gz
   
   cd etcd-v3.4.34-linux-amd64
   
   nohup ./etcd >/tmp/etcd.log 2>&1 &
   ```

3. 安装apisix 

   rpm 安装

   ```shell
   # 如果当前系统没有安装 OpenResty，请使用以下命令来安装 OpenResty 和 APISIX 仓库
   yum install -y https://repos.apiseven.com/packages/centos/apache-apisix-repo-1.0-1.noarch.rpm
   # 如果已安装 OpenResty 的官方 RPM 仓库，请使用以下命令安装 APISIX 的 RPM 仓库：
   yum-config-manager --add-repo https://repos.apiseven.com/packages/centos/apache-apisix.repo
   # 完成上述操作后使用以下命令安装 APISIX
   yum -y install apisix-3.9.1
   # 或者
   yum install apisix
   ```

   源码安装 https://github.com/apache/apisix/releases

   ```shell
   切换到 APISIX 源码的目录，创建依赖项并安装 APISIX，命令如下所示：
   cd apisix-${APISIX_VERSION}
   make deps
   make install 
   ```

   apisix通过源码安装在尝试安装golong的时候会连接超时Get "https://proxy.golang.org/google.golang.org/grpc/av/v1.55.1.zip": dial tcp 142.250.196.209:443: i/o timeout，目前通过DEB进行安装，安装目录在 /usr/local/apisix下

   

4. 安装完成后

   ```shell
   #APISIX 安装完成后，你可以运行以下命令初始化 NGINX 配置文件和 etcd：
   apisix init
   
   #使用以下命令启动 APISIX：
   apisix run
   
   # 测试是否成功安装
   curl -sL https://run.api7.ai/apisix/quickstart | sh
   # Server: APISIX/3.9.1
   ```

5. 端口问题

   apisix默认的网关端口 9080， 可以通过 /usr/local/apisix/conf/config-default.yaml 这个配置文件的 apisix.node_listen 这个配置更改。

   apisix默认的admin管理端口 9180，可以通过 /usr/local/apisix/conf/config-default.yaml 这个配置文件的 deployment.admin.admin_listen.port 这个配置更改。

   apisix-dashboard默认的端口 9000，可以通过 /usr/local/apisix/dashboard/conf/conf.yaml 配置文件的 conf.listen.port 进行修改。



# APISIX-dashboard安装

参考文章：[APISIX-dashboard安装篇_apisix dashboard安装-CSDN博客](https://blog.csdn.net/weixin_43117893/article/details/123018836)

1. 下载rpm包（github能够下载），然后使用 yum install 安装

2. 配置

   ```shell
   # 需要修改一下配置文件
   cd /usr/local/apisix/dashboard/conf
   vim conf.yaml
   # 修改allow_list，在后面追加一个 0.0.0.0/0 允许任何源的请求访问
   
   # run dashboard in the shell
   sudo manager-api -p /usr/local/apisix/dashboard/
   
   # or run dashboard as a service
   systemctl start apisix-dashboard
   ```


默认控制台地址：http://127.0.0.1:9000/



源码安装：

参考文章

https://apisix.apache.org/zh/docs/dashboard/install/

https://www.cnblogs.com/wangguishe/p/16165880.html



源码安装遇到的问题：

apisix-dashboard目前仅提供了rpm和源码安装，Ubuntu不支持rpm安装，只能使用源码安装。

- 需要配置go的代理网络环境 go env -w GOPROXY=https://goproxy.cn,direct
- 需要修改apisix-dashboard的build脚本Makefile文件，其中部分安装脚本有问题，目录的路径不对。(这是由于使用的go版本太低了，这里安装的apisix-dashboard版本是3.0，重新换了一个go1.23的版本就可以了，他这个过程中并没有报错，仅仅是提示一个 \+ cp ./api/conf/schema.json ./output/conf/schema.json cp: cannot stat './api/conf/schema.json': No such file or directory 命令失败，我这里重试修改了他的安装脚本，虽然安装成功，但是没有安装manage-api这个启动程序，并且使用make api-run也无法启动，并且make api-run这里报错了，一堆\# runtime ../../../go/src/runtime/mem_linux.go:20:6: sysAlloc redeclared in this block        ../../../go/src/runtime/mem.go:49:6: other declaration of sysAlloc类似这样的报错，chatgpt回答是go版本太低导致运行时的定义错误)。
- 需要修改yarn使用的node环境，由于系统自带了node10, 修改yarn程序的node环境为我们安装的node16，找到yarn的安装路径直接 vim yarn 

修改上述配置之后，大概差不多就安装完成了。

# 端口



apisix.node_listen 路由监听端口，请求都通过这个端口进行转发

plugin_attr.prometheus.export_addr.port 配置 Prometheus 插件的监控数据导出端口

deployment.admin.admin_listen.port 是用于配置 **Admin API** 的监听端口
