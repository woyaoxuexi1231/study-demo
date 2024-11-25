https://nginx.org/en/download.html

下载源码包 解压

```shell
# 编译前检查的命令
./configure
# 如果缺少依赖
yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel

# 编译
make
# 安装
make install 

nginx -c /etc/nginx/nginx.conf -s reload
```

