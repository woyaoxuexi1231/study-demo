[Linux系统设置全局代理（http代理，socks代理）_export sock proxy-CSDN博客](https://blog.csdn.net/yangxining/article/details/125118522)



### 临时

```shell
export http_proxy=http:192.168.3.14:7890
export https_proxy=http:192.168.3.14:7890

export ALL_PROXY=socks5://192.168.3.14:7890
```

### 永久

```shell
vim /etc/profile
http_proxy=http://192.168.3.14:7890
https_proxy=http://192.168.3.14:7890
ftp_proxy=http://192.168.3.14:7890
export http_proxy
export ftp_proxy
export https_proxy

```

