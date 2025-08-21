

# 更换

CentOS 7 已于 2024 年 6 月 30 日结束生命周期（EOL），官方已停止维护。许多镜像站可能已下架 CentOS 7 的仓库。

```shell
# 备份原文件
sudo cp /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak

# 使用阿里云镜像（示例）
sudo sed -e 's|^mirrorlist=|#mirrorlist=|g' \
         -e 's|^#baseurl=http://mirror.centos.org|baseurl=https://mirrors.aliyun.com|g' \
         -i /etc/yum.repos.d/CentOS-Base.repo

# 清理缓存并重建
sudo yum clean all && sudo yum makecache
```





# 还原默认

### **重新生成默认仓库**

#### **CentOS 7**

```bash
cd /etc/yum.repos.d/
rm -rf CentOS-*
rpm -Uvh --force http://vault.centos.org/7.9.2009/os/x86_64/Packages/centos-release-7-9.2009.0.el7.centos.x86_64.rpm

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
