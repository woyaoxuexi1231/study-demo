# 桥接模式

[Vmware 设置固定ip地址--桥接模式_vmware桥接模式固定ip-CSDN博客](https://blog.csdn.net/qq_39766779/article/details/124307651)

安装好系统后，Vmware上选择虚拟机对应的网络适配器为桥接模式即可。

需要修改以下内容：

```bash
$ cd /etc/sysconfig/network-scripts
$ vim ifcfg-ens33

# 原有内容
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
# 注释掉此行
#BOOTPROTO="dhcp"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="9de3bbfa-bcad-410e-a7e6-25247ed42746"
DEVICE="ens33"

# 修改为 yes
ONBOOT="yes"

# 新增内容
IPADDR="192.168.1.101"  # 这里就是你设置的固定IP
NETMASK="255.255.255.0" # 上图中IPv4 子网掩码，
GATEWAY="192.168.1.1"   # 上图中IPv4 默认网关
DNS1="192.168.1.1"      # 上图中IPv4 DNS服务器，可以不配


# 修改好后 :wq 保存退出
# 重启网关
$ service network restart

# 查看ip
$ ifconfig
```