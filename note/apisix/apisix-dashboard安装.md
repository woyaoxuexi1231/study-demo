# 安装

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