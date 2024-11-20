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



# 端口



apisix.node_listen 路由监听端口，请求都通过这个端口进行转发

plugin_attr.prometheus.export_addr.port 配置 Prometheus 插件的监控数据导出端口

deployment.admin.admin_listen.port 是用于配置 **Admin API** 的监听端口
