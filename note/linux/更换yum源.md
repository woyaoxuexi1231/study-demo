# 使用 wget

wget可能不可用，还是推荐使用 curl

```shell
# yum源
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo

# 其清空缓存
yum clean all
# 生成新的缓存
yum makecache
```



# 使用 curl

```shell
# 备份一下原始的镜像文件
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.back
# 配置阿里云的镜像
curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
# 重新生成缓存
yum makecache
```

可能存在依旧不行的情况，显示镜像依旧没改。这里有解决方案：https://blog.csdn.net/2301_81522768/article/details/143132834

```
cd /etc/yum.repos.d/
# 备份以下两个文件
cp CentOS-SCLo-scl.repo CentOS-SCLo-scl.repo.blk
cp CentOS-SCLo-scl-rh.repo CentOS-SCLo-scl-rh.repo.blk
```

vim CentOS-SCLo-scl.repo 

将此文件内容全部替换为以下内容

```
# CentOS-SCLo-sclo.repo
#
# Please see http://wiki.centos.org/SpecialInterestGroup/SCLo for more
# information
 
[centos-sclo-sclo]
name=CentOS-7 - SCLo sclo
baseurl=https://mirrors.aliyun.com/centos/7/sclo/x86_64/sclo/
gpgcheck=0
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-sclo-testing]
name=CentOS-7 - SCLo sclo Testing
baseurl=http://buildlogs.centos.org/centos/7/sclo/$basearch/sclo/
gpgcheck=0
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-sclo-source]
name=CentOS-7 - SCLo sclo Sources
baseurl=http://vault.centos.org/centos/7/sclo/Source/sclo/
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-sclo-debuginfo]
name=CentOS-7 - SCLo sclo Debuginfo
baseurl=http://debuginfo.centos.org/centos/7/sclo/$basearch/
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
```

vim CentOS-SCLo-scl-rh.repo

将此文件内容全部替换为以下内容

```
# CentOS-SCLo-rh.repo
#
# Please see http://wiki.centos.org/SpecialInterestGroup/SCLo for more
# information
 
[centos-sclo-rh]
name=CentOS-7 - SCLo rh
baseurl=https://mirrors.aliyun.com/centos/7/sclo/x86_64/rh/
gpgcheck=0
enabled=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-rh-testing]
name=CentOS-7 - SCLo rh Testing
baseurl=http://buildlogs.centos.org/centos/7/sclo/$basearch/rh/
gpgcheck=0
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-rh-source]
name=CentOS-7 - SCLo rh Sources
baseurl=http://vault.centos.org/centos/7/sclo/Source/rh/
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
 
[centos-sclo-rh-debuginfo]
name=CentOS-7 - SCLo rh Debuginfo
baseurl=http://debuginfo.centos.org/centos/7/sclo/$basearch/
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-SIG-SCLo
```

最后执行一下

```shell
yum clean all

yum makecache 
```





# 还原默认

### **重新生成默认仓库**

#### **CentOS 7**

```bash
sudo yum install -y centos-release
```

或手动下载官方源：

```bash
sudo curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo  # 阿里云镜像
# 或使用官方源
sudo curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrorlist.centos.org/?release=7&arch=x86_64&repo=os
```

#### **CentOS 8（或 Rocky Linux/AlmaLinux）**

```bash
sudo dnf install -y centos-release
# 或手动下载（如阿里云镜像）
sudo curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-8.repo
```

#### **RHEL 8/9**

```bash
bash

sudo subscription-manager repos --enable=rhel-8-for-x86_64-baseos-rpms  # 根据版本调整
```

### **清理缓存并重建**

```bash
sudo yum clean all    # 清理旧缓存
sudo yum makecache    # 生成新缓存
```
